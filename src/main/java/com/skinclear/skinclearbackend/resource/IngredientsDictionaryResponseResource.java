package com.skinclear.skinclearbackend.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsDictionaryResponseResource {
    private List<IngredientDictionaryResource> ingredients;
    private long totalIngredients;
}
