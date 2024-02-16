package com.skinclear.skinclearbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void updateFrom(IngredientInsight updatedInsight) {
        if (updatedInsight.getName() != null) {
            this.setName(updatedInsight.getName());
        }
        if (updatedInsight.getDescription() != null) {
            this.setDescription(updatedInsight.getDescription());
        }
        if (updatedInsight.getShortDescription() != null) {
            this.setShortDescription(updatedInsight.getShortDescription());
        }
        if (updatedInsight.getType() != null) {
            this.setType(updatedInsight.getType());
        }
        if (updatedInsight.getImage() != null) {
            this.setImage(updatedInsight.getImage());
        }
    }
}
