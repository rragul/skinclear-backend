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
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "like_count")
    private int likeCount;
    @Column(name = "dislike_count")
    private int dislikeCount;
    @Column(name = "what_it_is")
    private String whatItIs;
    @Column(name = "spf_rating")
    private int spfRating;
    @Column(name = "vegan")
    private boolean vegan;
    @Column(name = "category")
    private String category;
    @Column(name = "subcategory")
    private String subcategory;
    @Column(name = "alcohol_free")
    private boolean alcoholFree;

    @Column(name = "fragrance_free")
    private boolean fragranceFree;

    @Column(name = "silicone_free")
    private boolean siliconeFree;

    @Column(name = "sulfate_free")
    private boolean sulfateFree;

    @Column(name = "paraben_free")
    private boolean parabenFree;

    @Column(name = "oil_free")
    private boolean oilFree;

    @Column(name = "fungal_acne_safe")
    private boolean fungalAcneSafe;

    @Column(name = "eu_allergen_free")
    private boolean euAllergenFree;

    @Column(name = "reef_safe")
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
