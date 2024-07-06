package com.skinclear.skinclearbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private int like;
    private int dislike;
    private String WhatItIs;
    private int spfRating;
    private boolean vegan;
    private String category;
    private String subcategory;
    private boolean alcoholFree;
    private boolean fragranceFree;
    private boolean siliconeFree;
    private boolean sulfateFree;
    private boolean parabenFree;
    private boolean oilFree;
    private boolean fungalAcneSafe;
    private boolean euAllergenFree;
    private boolean reefSafe;
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_and_ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients;
}
