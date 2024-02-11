package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import com.skinclear.skinclearbackend.resource.IngredientsResource;

import java.util.List;

public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientsResource> getIngredients() {
       List<Ingredient> ingredients = ingredientRepository.findAll();
       for (Ingredient ingredient : ingredients) {
           IngredientsResource ingredientsResource = new IngredientsResource();
           ingredientsResource.setName(ingredient.getName());
       }
       return null;
    }
}
