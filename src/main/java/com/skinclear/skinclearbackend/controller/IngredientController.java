package com.skinclear.skinclearbackend.controller;


import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.resource.IngredientsResource;
import com.skinclear.skinclearbackend.service.IngredientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/ingredient")
public class IngredientController {

    private  final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientsResource> getIngredients(){
        return  ingredientService.getIngredients();
    }

    @GetMapping("/unstructured")
    public  List<Ingredient> getUnstructuredIngredient(){
        return  ingredientService.getUnstructuredIngredients();
    }
}
