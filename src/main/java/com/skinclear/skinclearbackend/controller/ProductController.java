package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.ProductDTO;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all")
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



    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getProductById(@PathVariable Long id){
        try {
            logger.info("request - getProductById | (URL: /api/v1/product/{}) | (Method: GET)", id);
            Product product = productService.getProductById(id);
            logger.info("response - getProductById | (URL: /api/v1/product/{}) | (Method: GET) | (status: 200)", id);
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(product)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getProductById | (URL: /api/v1/product/{}) | (Method: GET) | (status: 400)", id);
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error getting product")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<GeneralResponse> addProduct(@RequestBody ProductDTO product){
        logger.info("request - addProduct | (URL: /api/v1/product) | (Method: POST)");
        productService.addProduct(product);
        logger.info("response - addProduct | (URL: /api/v1/product) | (Method: POST) | (status: 201)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data("Product added successfully")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product){
        try {
            logger.info("request - updateProduct | (URL: /api/v1/product/{}) | (Method: PUT)", id);
            productService.updateProduct(id, product);
            logger.info("response - updateProduct | (URL: /api/v1/product/{}) | (Method: PUT) | (status: 200)", id);
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data("Product updated successfully")
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - updateProduct | (URL: /api/v1/product/{}) | (Method: PUT) | (status: 400)", id);
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error updating product")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

    @DeleteMapping
    public ResponseEntity<GeneralResponse> deleteProduct(@RequestBody List<Long> ids){
        logger.info("request - deleteProduct | (URL: /api/v1/product) | (Method: DELETE)");
        productService.deleteProduct(ids);
        logger.info("response - deleteProduct | (URL: /api/v1/product) | (Method: DELETE) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data("Product deleted successfully")
                        .build()
        );
    }

    @GetMapping("/recommendation")
    public ResponseEntity<GeneralResponse> getRecommendation(@RequestParam String ingredientName){
        logger.info("request - getRecommendation | (URL: /api/v1/product/recommendation) | (Method: GET)");
        List<Product> recommendation = productService.getRecommendation(ingredientName);
        logger.info("response - getRecommendation | (URL: /api/v1/product/recommendation) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(recommendation)
                        .build()
        );
    }

}
