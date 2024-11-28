package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private Long nextId = 1L;

    @Autowired
    private CategoryRepository categoryRepository;

//    List<Category> categories = categoryRepository.findAll();
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
//        categories.add(category);
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

//        categoryRepository.delete(category);
        categoryRepository.deleteById(categoryId);
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
//        List<Category> categories = categoryRepository.findAll();
//        Optional<Category> toBeUpdatedCategory = categories.stream()
//                .filter( c-> c.getCategoryId().equals(categoryId))
//                .findFirst();
//
//        if(toBeUpdatedCategory.isPresent()) {
//            Category exixtingCategroy = toBeUpdatedCategory.get();
//            exixtingCategroy.setCategoryName(category.getCategoryName());
//            Category savedCategory = categoryRepository.save(exixtingCategroy);
//            return savedCategory;
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with categoryId:"+ categoryId+" Not Found");
//        }
//
//        new logic to get optional and not to get all the categories just fetch by ID
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
//        categoryRepository.save(savedCategory);
        // working both way typed above its just typed above
        return savedCategory;

    }

}
