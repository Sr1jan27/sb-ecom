package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%');
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct  = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Product product = modelMapper.map(productDTO, Product.class);
        productDTO.setProductId(productId);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());

        existingProduct = productRepository.save(existingProduct);
        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product toBeDeletedPoduct  = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(toBeDeletedPoduct);
        return modelMapper.map(toBeDeletedPoduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // get product from db
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product", "productId", productId));

        String path = "images/";
        String fileName = uploadImage(path, image);
        productFromDB.setImage(fileName);
        Product updatedProduct = productRepository.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getName(); // this will return  just the parameter name of the file not whole file name with extension
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath = path + File.pathSeparator +fileName;

        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;

    }
}
