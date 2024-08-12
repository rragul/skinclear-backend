package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.resource.Error;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.IconResource;
import com.skinclear.skinclearbackend.dto.IngredientInsightDTO;
import com.skinclear.skinclearbackend.service.IngredientInsightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://d2azwxcl0602xy.cloudfront.net, http://localhost:3000")
@RequestMapping("/api/v1/ingredient-insight")
public class IngredientInsightController extends AbstractController{

    private static final Logger logger = LoggerFactory.getLogger(IngredientInsightController.class);

    private final IngredientInsightService ingredientInsightService;

    public IngredientInsightController(IngredientInsightService ingredientInsightService) {
        this.ingredientInsightService = ingredientInsightService;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getIngredientInsights(@RequestParam int page , @RequestParam int size) {
        logger.info("request - getIngredientInsights | (URL: /api/v1/ingredient-insight) | (Method: GET) | (page: {}) | (size: {})", page, size);
        Page<IngredientInsight> ingredientInsightWithPagination = ingredientInsightService.getIngredientInsightWithPagination(page, size);
        logger.info("response - getIngredientInsights | (URL: /api/v1/ingredient-insight) | (Method: GET) | (page: {}) | (size: {})", page, size);
        return ResponseEntity.ok().body(
                GeneralResponse.builder()
                        .success(true)
                        .data(ingredientInsightWithPagination)
                        .build()
        );
    }

    @GetMapping("/type/{typeName}")
    public ResponseEntity<GeneralResponse> getIngredientInsightsByType(@PathVariable String typeName) {
        logger.info("request - getIngredientInsightsByType | (URL: /api/v1/ingredient-insight/type/{}) | (Method: GET)", typeName);
        List<IngredientInsight> ingredientInsights = ingredientInsightService.getIngredientsByType(typeName);
        logger.info("response - getIngredientInsightsByType | (URL: /api/v1/ingredient-insight/type/{}) | (Method: GET)", typeName);
        return ResponseEntity.ok().body(
                GeneralResponse.builder()
                        .success(true)
                        .data(ingredientInsights)
                        .build()
        );
    }

    @GetMapping("/random-images")
    public ResponseEntity<GeneralResponse> getRandomImages(@RequestParam(value = "limit", defaultValue = "28") int limit) {
        logger.info("request - getRandomImages | (URL: /api/v1/ingredient-insight/random-images) | (Method: GET) | (limit: {})", limit);
        List<IconResource> iconResources = ingredientInsightService.getRandomImages(limit);
        logger.info("response - getRandomImages | (URL: /api/v1/ingredient-insight/random-images) | (Method: GET) | (limit: {})", limit);
        return ResponseEntity.ok().body(
                GeneralResponse.builder()
                        .success(true)
                        .data(iconResources)
                        .build()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<GeneralResponse> addIngredientInsights(@RequestBody IngredientInsightDTO ingredientInsight) {
        try {
            logger.info("request - addIngredientInsights | (URL: /api/v1/ingredient-insight/add) | (Method: POST)");
            ingredientInsightService.addIngredientInsight(ingredientInsight);
            logger.info("response - addIngredientInsights | (URL: /api/v1/ingredient-insight/add) | (Method: POST)");
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Ingredient Insight added successfully")
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("Error adding ingredient insight", e);
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error adding ingredient insight")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateIngredientInsights(@RequestBody IngredientInsightDTO ingredientInsight, @PathVariable Long id) {
        try{
            logger.info("request - updateIngredientInsights | (URL: /api/v1/ingredient-insight/update/{}) | (Method: PUT)", id);
            ingredientInsightService.updateIngredientInsight(ingredientInsight, id);
            logger.info("response - updateIngredientInsights | (URL: /api/v1/ingredient-insight/update/{}) | (Method: PUT)", id);
            return ResponseEntity.ok().body(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Ingredient Insight updated successfully")
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("Error updating ingredient insight", e);
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error updating ingredient insight")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralResponse> deleteIngredientInsights(@RequestBody List<Long> ids) {
        try {
            logger.info("request - deleteIngredientInsights | (URL: /api/v1/ingredient-insight/delete) | (Method: DELETE)");
            ingredientInsightService.deleteIngredientInsight(ids);
            logger.info("response - deleteIngredientInsights | (URL: /api/v1/ingredient-insight/delete) | (Method: DELETE)");
            return ResponseEntity.ok().body(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Ingredient Insight deleted successfully")
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("Error deleting ingredient insight", e);
            return ResponseEntity.badRequest().body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Error deleting ingredient insight")
                            .error(Error.builder().message(e.getMessage()).build())
                            .errorType(e.getClass().getName())
                            .build()
            );
        }
    }

}
