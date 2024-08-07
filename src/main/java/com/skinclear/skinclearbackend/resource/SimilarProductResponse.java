package com.skinclear.skinclearbackend.resource;

import com.skinclear.skinclearbackend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimilarProductResponse {
    private Product targetProduct;
    private List<ProductMatch> similarProducts;

    @Data
    @AllArgsConstructor
    public static class ProductMatch {
        private Product product;
        private double ingredientMatchPercentage;
        private double attributeMatchPercentage;
        private double matchPercentage;
    }
}
