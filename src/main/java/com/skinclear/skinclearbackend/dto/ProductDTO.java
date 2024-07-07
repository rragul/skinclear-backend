package com.skinclear.skinclearbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String type;
    private int like;
    private int dislike;
    private String WhatItIs;
    private int spfRating;
    private boolean vegan;
    private String category;
    private String subcategory;
    private Long brandId;
    private Long[] ingredientsIDS;
}
