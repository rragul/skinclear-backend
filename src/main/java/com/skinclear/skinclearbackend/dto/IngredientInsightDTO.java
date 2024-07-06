package com.skinclear.skinclearbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientInsightDTO {
    private String name;
    private String shortDescription;
    private String type;
    private String image;
    private String description;
}
