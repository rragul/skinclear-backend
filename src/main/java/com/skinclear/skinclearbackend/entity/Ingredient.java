package com.skinclear.skinclearbackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String whatItDoes;
    private String otherNames;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    @Column(columnDefinition = "TEXT")
    private String explain;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ingredient_and_what_it_is_insights",
            joinColumns = @JoinColumn(name = "ingredient_what_it_isid"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_insight_what_it_is_id"))
    private Set<IngredientInsight> whatItIs;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ingredient_and_benefits_insights",
            joinColumns = @JoinColumn(name = "ingredient_benefits_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_insight_benefits_id"))
    private Set<IngredientInsight> benefits;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ingredient_and_concern_insights",
            joinColumns = @JoinColumn(name = "ingredient_concern_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_insight_concern_id"))
    private Set<IngredientInsight> concern;

    public Ingredient(String name, String whatItDoes, Set<IngredientInsight> benefits, String otherNames,
                      Set<IngredientInsight> concern, String rarity, Integer likeCount, Integer dislikeCount, String explain, Set<IngredientInsight> whatItIs) {
        this.name = name;
        this.whatItDoes = whatItDoes;
        this.benefits = benefits;
        this.otherNames = otherNames;
        this.concern = concern;
        this.rarity = rarity;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.explain = explain;
        this.whatItIs = whatItIs;
    }

    public void updateFrom(Ingredient ingredient) {
        if (ingredient.getName() != null) {
            this.setName(ingredient.getName());
        }
        if (ingredient.getWhatItDoes() != null) {
            this.setWhatItDoes(ingredient.getWhatItDoes());
        }
        if (ingredient.getOtherNames() != null) {
            this.setOtherNames(ingredient.getOtherNames());
        }
        if (ingredient.getRarity() != null) {
            this.setRarity(ingredient.getRarity());
        }
        if (ingredient.getLikeCount() != null) {
            this.setLikeCount(ingredient.getLikeCount());
        }
        if (ingredient.getDislikeCount() != null) {
            this.setDislikeCount(ingredient.getDislikeCount());
        }
        if (ingredient.getExplain() != null) {
            this.setExplain(ingredient.getExplain());
        }
    }
}
