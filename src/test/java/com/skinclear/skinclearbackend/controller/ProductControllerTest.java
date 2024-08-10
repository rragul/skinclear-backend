package com.skinclear.skinclearbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinclear.skinclearbackend.dto.ProductDTO;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.SimilarProductResponse;
import com.skinclear.skinclearbackend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(new Product());
        when(productService.getAllProduct()).thenReturn(products);

        ResponseEntity<GeneralResponse> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(products, response.getBody().getData());
        verify(productService, times(1)).getAllProduct();
    }

    @Test
    void testGetAllProductsWithPagination() {
        Page<Product> productPage = new PageImpl<>(List.of(new Product()));
        when(productService.getAllProductWithPagination(anyInt(), anyInt())).thenReturn(productPage);

        ResponseEntity<GeneralResponse> response = productController.getAllProductsWithPagination(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(productPage, response.getBody().getData());
        verify(productService, times(1)).getAllProductWithPagination(0, 10);
    }

    @Test
    void testGetProductsBySubCategoryNameWithPagination() {
        Page<Product> productPage = new PageImpl<>(List.of(new Product()));
        when(productService.getProductsBySubCategoryNameWithPagination(anyString(), anyInt(), anyInt())).thenReturn(productPage);

        ResponseEntity<GeneralResponse> response = productController.getProductsBySubCategoryNameWithPagination("subCategory", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(productPage, response.getBody().getData());
        verify(productService, times(1)).getProductsBySubCategoryNameWithPagination("subCategory", 0, 10);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        when(productService.getProductById(anyLong())).thenReturn(product);

        ResponseEntity<GeneralResponse> response = productController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(product, response.getBody().getData());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void testGetProductByName() {
        Product product = new Product();
        when(productService.getProductByName(anyString())).thenReturn(product);

        ResponseEntity<GeneralResponse> response = productController.getProductByName("name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(product, response.getBody().getData());
        verify(productService, times(1)).getProductByName("name");
    }

    @Test
    void testGetSimilarProducts() {
        SimilarProductResponse similarProducts = new SimilarProductResponse();
        when(productService.getSimilarProducts(anyLong(), anyInt(), anyInt())).thenReturn(similarProducts);

        ResponseEntity<GeneralResponse> response = productController.getSimilarProducts(1L, 0, 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(similarProducts, response.getBody().getData());
        verify(productService, times(1)).getSimilarProducts(1L, 3, 0);
    }

    @Test
    void testAddProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        MultipartFile image = mock(MultipartFile.class);
        when(objectMapper.readValue(anyString(), eq(ProductDTO.class))).thenReturn(productDTO);

        ResponseEntity<GeneralResponse> response = productController.addProduct(image, "{}");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Product added successfully", response.getBody().getMessage());
        verify(productService, times(1)).addProduct(productDTO, image);
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        MultipartFile image = mock(MultipartFile.class);
        when(objectMapper.readValue(anyString(), eq(ProductDTO.class))).thenReturn(productDTO);

        ResponseEntity<GeneralResponse> response = productController.updateProduct(1L, image, "{}");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Product updated successfully", response.getBody().getMessage());
        verify(productService, times(1)).updateProduct(1L, productDTO, image);
    }

    @Test
    void testDeleteProduct() {
        List<Long> ids = List.of(1L, 2L);

        ResponseEntity<GeneralResponse> response = productController.deleteProduct(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Product deleted successfully", response.getBody().getMessage());
        verify(productService, times(1)).deleteProduct(ids);
    }

    @Test
    void testGetRecommendation() {
        List<Product> products = List.of(new Product());
        when(productService.getRecommendation(anyLong())).thenReturn(products);

        ResponseEntity<GeneralResponse> response = productController.getRecommendation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(products, response.getBody().getData());
        verify(productService, times(1)).getRecommendation(1L);
    }
    @Test
    void testSearchProduct() {
        List<Product> products = List.of(new Product());
        when(productService.searchProduct(anyString())).thenReturn(products);

        ResponseEntity<GeneralResponse> response = productController.searchProduct("keyword");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(products, response.getBody().getData());
        verify(productService, times(1)).searchProduct("keyword");
    }
}
