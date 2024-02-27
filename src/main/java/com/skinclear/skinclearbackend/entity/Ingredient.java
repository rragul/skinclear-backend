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
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String typeId;
    private String whatItDoes;
    private String benefitsId;
    private String otherNames;
    private String concernId;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    @Column(columnDefinition = "TEXT")
    private String explain;

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
