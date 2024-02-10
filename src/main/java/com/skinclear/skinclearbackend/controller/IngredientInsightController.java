package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.service.IngredientInsightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredient-insight")
public class IngredientInsightController {

    private final IngredientInsightService ingredientInsightService;

    public IngredientInsightController(IngredientInsightService ingredientInsightService) {
        this.ingredientInsightService = ingredientInsightService;
    }

    @GetMapping
    public List<IngredientInsight> getIngredientInsights() {
        return ingredientInsightService.getIngredientInsights();
    }
}
