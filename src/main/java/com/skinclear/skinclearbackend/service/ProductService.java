package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
}
