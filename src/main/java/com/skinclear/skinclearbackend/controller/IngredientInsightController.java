package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.resource.IconResource;
import com.skinclear.skinclearbackend.resource.IngredientInsightsResponseResource;
import com.skinclear.skinclearbackend.service.IngredientInsightService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> getIngredientInsights(@RequestParam int page ,@RequestParam int size) {
        IngredientInsightsResponseResource response = ingredientInsightService.getIngredientInsightWithPagination(page,size);
        return sendSuccessResponse(response);
    }

    @GetMapping("/random-images")
    public ResponseEntity<Object> getRandomImages(@RequestParam(value = "limit", defaultValue = "28") int limit) {
       List<IconResource> icons = ingredientInsightService.getRandomImages(limit);
       return sendSuccessResponse(icons);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addIngredientInsights(@RequestBody IngredientInsight ingredientInsight) {
        boolean isAdded = ingredientInsightService.addIngredientInsight(ingredientInsight);
        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient Insight added successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ingredient Insight already exists");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateIngredientInsights(@RequestBody IngredientInsight ingredientInsight, @PathVariable Long id) {
        boolean isUpdated = ingredientInsightService.updateIngredientInsight(ingredientInsight, id);
        if (isUpdated) {
            return ResponseEntity.ok("Ingredient Insight updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient Insight does not exist");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteIngredientInsights(@RequestBody List<Long> ids) {
        boolean isDeleted = ingredientInsightService.deleteIngredientInsight(ids);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient Insight does not exist");
    }

}
