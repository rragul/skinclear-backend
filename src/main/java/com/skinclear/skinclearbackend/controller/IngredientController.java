package com.skinclear.skinclearbackend.controller;


import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.resource.IngredientResource;
import com.skinclear.skinclearbackend.resource.IngredientsDictionaryResponseResource;
import com.skinclear.skinclearbackend.service.IngredientService;
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

    @GetMapping("/structured")
    public ResponseEntity<Object> getStructuredIngredients(@RequestParam int page, @RequestParam int size){
        IngredientsDictionaryResponseResource ingredientResponseResource = ingredientService.getStructuredIngredients(page, size);
        return sendSuccessResponse(ingredientResponseResource);
    }

    @GetMapping("/structured/{id}")
    public ResponseEntity<Object> getStructuredIngredient(@PathVariable Long id){
        IngredientResource ingredient = ingredientService.getIngredientById(id);
        return sendSuccessResponse(ingredient);
    }

    @GetMapping("/unstructured")
    public  List<Ingredient> getUnstructuredIngredient(){
        return  ingredientService.getUnstructuredIngredients();
    }
}
