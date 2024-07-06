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

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void addProduct(Product product) {
       productRepository.findProductByName(product.getName())
                .ifPresent(p -> {
                    throw new RuntimeException("Product already exists with name: " + product.getName());
                });
        productRepository.save(product);
    }

    public void updateProduct(Long id, Product product) {
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productRepository.findProductByName(product.getName())
                .ifPresent(p -> {
                    throw new RuntimeException("Product already exists with name: " + product.getName());
                });
        product.setId(id);
        productRepository.save(product);
    }

    public void deleteProduct(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }
}
