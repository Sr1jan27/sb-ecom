package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        return null;
    }
}
