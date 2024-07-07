package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.IngredientDTO;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/ingredient")
public class IngredientController extends AbstractController{

    private final static Logger logger = LoggerFactory.getLogger(IngredientController.class);

    private  final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllIngredientsWithPagination(@RequestParam int page, @RequestParam int size){
        logger.info("request - getAllIngredientsWithPagination | (URL: /api/v1/ingredient) | (Method: GET) | (page: {}) | (size: {})", page, size);
        Object allIngredientWithPagination = ingredientService.getAllIngredientWithPagination(page, size);
        logger.info("response - getAllIngredientsWithPagination | (URL: /api/v1/ingredient) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(allIngredientWithPagination)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getIngredientById(@PathVariable Long id) {
        logger.info("request - getIngredientById | (URL: /api/v1/ingredient/{id}) | (Method: GET) | (id: {})", id);
        Ingredient ingredientById = ingredientService.getIngredientById(id);
        logger.info("response - getIngredientById | (URL: /api/v1/ingredient/{id}) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(ingredientById)
                        .build()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<GeneralResponse> addIngredient(@RequestBody IngredientDTO ingredient) {
        logger.info("request - addIngredient | (URL: /api/v1/ingredient/add) | (Method: POST) | (ingredient: {})", ingredient);
        ingredientService.addIngredient(ingredient);
        logger.info("response - addIngredient | (URL: /api/v1/ingredient/add) | (Method: POST) | (status: 201)");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                GeneralResponse.builder()
                        .success(true)
                        .message("Ingredient added successfully")
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralResponse> deleteBrand(@RequestBody List<Long> ids) {
        logger.info("request - deleteBrand | (URL: /api/v1/ingredient/delete) | (Method: DELETE) | (ids: {})", ids);
        ingredientService.deleteIngredient(ids);
        logger.info("response - deleteBrand | (URL: /api/v1/ingredient/delete) | (Method: DELETE) | (status: 200)");
        return ResponseEntity.status(HttpStatus.OK).body(
                GeneralResponse.builder()
                        .success(true)
                        .message("Ingredient deleted successfully")
                        .build()
        );

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralResponse> updateBrand(@RequestBody IngredientDTO ingredient, @PathVariable Long id) {
        logger.info("request - updateBrand | (URL: /api/v1/ingredient/update/{id}) | (Method: PUT) | (ingredient: {}) | (id: {})", ingredient, id);
        ingredientService.updateIngredient(ingredient, id);
        logger.info("response - updateBrand | (URL: /api/v1/ingredient/update/{id}) | (Method: PUT) | (status: 200)");
        return ResponseEntity.status(HttpStatus.OK).body(
                GeneralResponse.builder()
                        .success(true)
                        .message("Ingredient updated successfully")
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse> getIngredientsByKeywords(@RequestParam String keyword) {
        logger.info("request - getIngredientsByKeywords | (URL: /api/v1/ingredient/search}) | (Method: GET) | (keyword: {})", keyword);
        List<Ingredient> ingredients = ingredientService.searchIngredientsByName(keyword);
        logger.info("response - getIngredientsByKeywords | (URL: /api/v1/ingredient/search) | (Method: GET) | (status: 200)");
        return ResponseEntity.ok(
                GeneralResponse.builder()
                        .success(true)
                        .data(ingredients)
                        .build()
        );
    }
}
