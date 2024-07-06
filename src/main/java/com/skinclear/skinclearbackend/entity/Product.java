package com.skinclear.skinclearbackend.entity;

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
    //private Brand brand;
    private int like;
    private int dislike;
    private String WhatItIs;
    private int spfRating;
    private boolean vegan;
   // private List<Ingredient> ingredients;


}
