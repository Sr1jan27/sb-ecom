package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories = new ArrayList<>();

    private Long nextId = 1L;
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        categories.remove(category);
        return "Category with categoryId:"+ category.getCategoryId() + " deleted";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
//        Category toBeUpdatedCategory = categories.stream()
//                .filter( c-> c.getCategoryId().equals(categoryId))
//                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));
//        toBeUpdatedCategory.setCategoryName(category.getCategoryName());
//        return toBeUpdatedCategory;

        // or we can write the same logic as
        Optional<Category> toBeUpdatedCategory = categories.stream()
                .filter( c-> c.getCategoryId().equals(categoryId))
                .findFirst();

        if(toBeUpdatedCategory.isPresent()) {
            Category exixtingCategroy = toBeUpdatedCategory.get();
            exixtingCategroy.setCategoryName(category.getCategoryName());
            return exixtingCategroy;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with categoryId:"+ categoryId+" Not Found");
        }


    }

}
