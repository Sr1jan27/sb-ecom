package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getProductByCategoryId(Long categoryId);

    ProductResponse getProductByKeyword(String keyword);

    ProductDTO updateProduct(Product product, Long productId);

    ProductDTO deleteProduct(Long productId);
}
