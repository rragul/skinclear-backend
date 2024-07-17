package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.ProductDTO;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        logger.info("request - getAllProductsWithPagination | (URL: /api/v1/product) | (Method: GET) | (page: {}) | (size: {})", page, size);
        Object allProductWithPagination = productService.getAllProductWithPagination(page, size);
        logger.info("response - getAllProductsWithPagination | (URL: /api/v1/product) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(allProductWithPagination)
                        .build()
        );
    }

    @GetMapping("/sub-category")
    public ResponseEntity<GeneralResponse> getProductsBySubCategoryNameWithPagination(@RequestParam String subCategory, @RequestParam int page, @RequestParam int size){
        logger.info("request - getProductsBySubCategoryByNameWithPagination | (URL: /api/v1/product/sub-category) | (Method: GET) | (subCategory: {}) | (page: {}) | (size: {})", subCategory, page, size);
        Object productsBySubCategoryByNameWithPagination = productService.getProductsBySubCategoryNameWithPagination(subCategory, page, size);
        logger.info("response - getProductsBySubCategoryByNameWithPagination | (URL: /api/v1/product/sub-category) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(productsBySubCategoryByNameWithPagination)
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

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete")
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
    public ResponseEntity<GeneralResponse> getRecommendation(@RequestParam Long ingredientId){
        try{
            logger.info("request - getRecommendation | (URL: /api/v1/product/recommendation) | (Method: GET)");
            List<Product> recommendation = productService.getRecommendation(ingredientId);
            logger.info("response - getRecommendation | (URL: /api/v1/product/recommendation) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(recommendation)
                            .build()
            );
        }
        catch (Exception e){
            logger.error("response - getRecommendation | (URL: /api/v1/product/recommendation) | (Method: GET) | (status: 400)");
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error getting recommendation")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }
    @GetMapping("/filter")
    public ResponseEntity<GeneralResponse> getProductsByFilter(
            @RequestParam(required = false) String subCategory,
            @RequestParam(required = false) String preference,
            @RequestParam(required = false) String benefits,
            @RequestParam(required = false) String whatItIs,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("request - getProductsByFilter | (URL: /api/v1/product/filter) | (Method: GET)");
            Page<Product> productsByFilter = productService.getProductsByFilter(subCategory, preference, benefits, whatItIs, ingredient, brand, page, size);
            logger.info("response - getProductsByFilter | (URL: /api/v1/product/filter) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(productsByFilter.getContent())
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - getProductsByFilter | (URL: /api/v1/product/filter) | (Method: GET) | (status: 400)");
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error getting products by filter")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse> searchProduct(@RequestParam String keyword){
        try {
            logger.info("request - searchProduct | (URL: /api/v1/product/search) | (Method: GET)");
            List<Product> products = productService.searchProduct(keyword);
            logger.info("response - searchProduct | (URL: /api/v1/product/search) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(products)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - searchProduct | (URL: /api/v1/product/search) | (Method: GET) | (status: 400)");
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error searching product")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

}
