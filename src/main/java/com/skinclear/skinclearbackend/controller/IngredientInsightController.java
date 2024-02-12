package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.resource.IconResource;
import com.skinclear.skinclearbackend.service.IngredientInsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/ingredient-insight")
public class IngredientInsightController extends AbstractController{

    private final IngredientInsightService ingredientInsightService;

    public IngredientInsightController(IngredientInsightService ingredientInsightService) {
        this.ingredientInsightService = ingredientInsightService;
    }

    @GetMapping
    public List<IngredientInsight> getIngredientInsights() {
        return ingredientInsightService.getIngredientInsights();
    }

    @GetMapping("/random-images")
    public ResponseEntity<Object> getRandomImages(@RequestParam(value = "limit", defaultValue = "28") int limit) {
       List<IconResource> icons = ingredientInsightService.getRandomImages(limit);
       return sendSuccessResponse(icons);
    }
}
