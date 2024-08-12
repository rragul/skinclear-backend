package com.skinclear.skinclearbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinclear.skinclearbackend.dto.ProductDTO;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.SimilarProductResponse;
import com.skinclear.skinclearbackend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/product")
public class ProductController extends AbstractController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private  final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
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

    @GetMapping("/name")
    public ResponseEntity<GeneralResponse> getProductByName(@RequestParam String name){
        try {
            logger.info("request - getProductByName | (URL: /api/v1/product/name) | (Method: GET)");
            Product product = productService.getProductByName(name);
            logger.info("response - getProductByName | (URL: /api/v1/product/name) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(product)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getProductByName | (URL: /api/v1/product/name) | (Method: GET) | (status: 400)");
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

   @GetMapping("/similar")
    public ResponseEntity<GeneralResponse> getSimilarProducts(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
   ){
        try {
            logger.info("request - getSimilarProducts | (URL: /api/v1/product/similar) | (Method: GET)");
            SimilarProductResponse similarProducts = productService.getSimilarProducts(productId, size, page);
            logger.info("response - getSimilarProducts | (URL: /api/v1/product/similar) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(similarProducts)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getSimilarProducts | (URL: /api/v1/product/similar) | (Method: GET) | (status: 400)");
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error getting similar products")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<GeneralResponse> addProduct(
            @RequestParam("image") MultipartFile image,
            @RequestPart("product") String productJson) {
        logger.info("request - addProduct | (URL: /api/v1/product) | (Method: POST)");

        try {
            // Parse JSON string to ProductDTO
            ProductDTO product = objectMapper.readValue(productJson, ProductDTO.class);
            productService.addProduct(product, image);
            logger.info("response - addProduct | (URL: /api/v1/product) | (Method: POST) | (status: 201)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Product added successfully")
                            .build()
            );
        } catch (Exception e) {
            logger.error("Error adding product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error adding product")
                            .build()
            );
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateProduct(@PathVariable Long id,
                                                         @RequestParam(value = "image", required = false) MultipartFile image,
                                                         @RequestPart("product") String productJson){
        try {
            logger.info("request - updateProduct | (URL: /api/v1/product/{}) | (Method: PUT)", id);
            // Parse JSON string to ProductDTO
            ProductDTO product = objectMapper.readValue(productJson, ProductDTO.class);
            productService.updateProduct(id, product, image);
            logger.info("response - updateProduct | (URL: /api/v1/product/{}) | (Method: PUT) | (status: 200)", id);
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Product updated successfully")
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
        try {
            logger.info("request - deleteProduct | (URL: /api/v1/product) | (Method: DELETE)");
            productService.deleteProduct(ids);
            logger.info("response - deleteProduct | (URL: /api/v1/product) | (Method: DELETE) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Product deleted successfully")
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - deleteProduct | (URL: /api/v1/product) | (Method: DELETE) | (status: 400)");
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error deleting product")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
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
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("request - getProductsByFilter | (URL: /api/v1/product/filter) | (Method: GET)");
            Page<Product> productsByFilter = productService.getProductsByFilter(subCategory, preference, benefits, type, ingredient, brand, page, size);
            logger.info("response - getProductsByFilter | (URL: /api/v1/product/filter) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(productsByFilter)
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
