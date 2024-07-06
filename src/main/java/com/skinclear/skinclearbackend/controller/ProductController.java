package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/product")
public class ProductController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private  final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllProducts(){
        logger.info("request - getAllProducts | (URL: /api/v1/product) | (Method: GET)");
        List<Product> allProducts = productService.getAllProduct();
        logger.info("response - getAllProducts | (URL: /api/v1/product) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(allProducts)
                        .build()
        );
    }
}
