package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/ingredient")
public class IngredientController extends AbstractController{

    private  final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllIngredientsWithPagination(@RequestParam int page, @RequestParam int size){
        return sendSuccessResponse(ingredientService.getAllIngredientWithPagination(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBrandById(@PathVariable Long id) {
        return sendSuccessResponse(ingredientService.getIngredientById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addIngredient(@RequestBody Ingredient ingredient) {
        boolean isAdded = ingredientService.addBrand(ingredient);
        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingredient added successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ingredient already exists");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBrand(@RequestBody List<Long> ids) {
        boolean isDeleted = ingredientService.deleteBrand(ids);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBrand(@RequestBody Ingredient ingredient, @PathVariable Long id) {
        String message = ingredientService.updateBrand(ingredient, id);
        if (message.equals("Ingredient updated successfully")) {
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
