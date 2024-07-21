package com.skinclear.skinclearbackend.resource;

import com.skinclear.skinclearbackend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimilarProductResponse {
    private Product product;
    private double ingredientMatchPercentage;
    private double attributeMatchPercentage;
    private double matchPercentage;
}
