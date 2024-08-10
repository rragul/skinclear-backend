package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.Product;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import com.skinclear.skinclearbackend.resource.SimilarProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private BrandService brandService;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProduct() {
        Product product = new Product();
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<Product> products = productService.getAllProduct();

        assertNotNull(products);
        assertEquals(1, products.size());
    }

    @Test
    void getProductById() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
    }


    @Test
    void deleteProduct() {
        productService.deleteProduct(Collections.singletonList(1L));

        verify(productRepository, times(1)).deleteAllById(Collections.singletonList(1L));
    }

    @Test
    void getRecommendation() {
        Ingredient ingredient = new Ingredient();
        when(ingredientService.getIngredientById(1L)).thenReturn(ingredient);

        Product product = new Product();
        when(productRepository.findProductByIngredients(ingredient)).thenReturn(Collections.singletonList(product));

        List<Product> recommendations = productService.getRecommendation(1L);

        assertNotNull(recommendations);
        assertEquals(1, recommendations.size());
    }

    @Test
    void getSimilarProducts() {
        Product targetProduct = new Product();
        targetProduct.setId(1L);

        Ingredient ingredient = new Ingredient();
        targetProduct.setIngredients(Collections.singletonList(ingredient));
        when(productRepository.findById(1L)).thenReturn(Optional.of(targetProduct));

        Product similarProduct = new Product();
        similarProduct.setId(2L);
        similarProduct.setIngredients(Collections.singletonList(ingredient));
        when(productRepository.findSimilarProducts(1L)).thenReturn(Collections.singletonList(similarProduct));

        SimilarProductResponse response = productService.getSimilarProducts(1L, 10, 0);

        assertNotNull(response);
        assertEquals(1, response.getSimilarProducts().size());
        assertEquals(2L, response.getSimilarProducts().get(0).getProduct().getId());
    }
}
