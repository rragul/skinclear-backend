package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientInsightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientInsightService {

    private final IngredientInsightRepository ingredientInsightRepository;

    public IngredientInsightService(IngredientInsightRepository ingredientInsightRepository) {
        this.ingredientInsightRepository = ingredientInsightRepository;
    }

    public List<IngredientInsight> getIngredientInsights() {
        return ingredientInsightRepository.findAll();
    }

}
