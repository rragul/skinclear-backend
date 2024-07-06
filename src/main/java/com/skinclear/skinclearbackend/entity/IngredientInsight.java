package com.skinclear.skinclearbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String shortDescription;
    private String type;
    private String image;
    @Column(length = 1000)
    private String description;

    @JsonBackReference
    @ManyToMany(mappedBy = "whatItIs")
    private Set<Ingredient> whatItIsIngredients;

    @JsonBackReference
    @ManyToMany(mappedBy = "benefits")
    private Set<Ingredient> benefitsIngredients;

    @JsonBackReference
    @ManyToMany(mappedBy = "concern")
    private Set<Ingredient> concernIngredients;

    public IngredientInsight(String name, String type, String image, String shortDescription, String description) {
        this.name = name;
        this.type = type;
        this.image = image;
        this.shortDescription = shortDescription;
        this.description = description;
    }
}
