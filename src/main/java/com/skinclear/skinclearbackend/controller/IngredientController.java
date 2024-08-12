package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.IngredientDTO;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.IngredientResponse;
import com.skinclear.skinclearbackend.service.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/ingredient")
public class IngredientController extends AbstractController{

    private final static Logger logger = LoggerFactory.getLogger(IngredientController.class);

    private  final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllIngredientsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            logger.info("request - getAllIngredientsWithPagination | (URL: /api/v1/ingredient) | (Method: GET) | (page: {}) | (size: {})", page, size);
            Page<IngredientResponse> allIngredientWithPagination = ingredientService.getAllIngredientWithPagination(page, size);
            logger.info("response - getAllIngredientsWithPagination | (URL: /api/v1/ingredient) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(allIngredientWithPagination)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getIngredientById | (URL: /api/v1/ingredient/{id}) | (Method: GET) | (status: 404)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getIngredientById(@PathVariable Long id) {
        try {
            logger.info("request - getIngredientById | (URL: /api/v1/ingredient/{id}) | (Method: GET) | (id: {})", id);
            IngredientResponse ingredientResponse = ingredientService.getIngredientResponseById(id);
            logger.info("response - getIngredientById | (URL: /api/v1/ingredient/{id}) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(ingredientResponse)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getIngredientById | (URL: /api/v1/ingredient/{id}) | (Method: GET) | (status: 404)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Ingredient not found")
                            .build()
            );
        }
    }

    @GetMapping("/name")
    public ResponseEntity<GeneralResponse> getIngredientByName(@RequestParam String name) {
        try {
            logger.info("request - getIngredientByName | (URL: /api/v1/ingredient/name) | (Method: GET) | (name: {})", name);
            IngredientResponse ingredientResponse = ingredientService.getIngredientResponseByName(name);
            logger.info("response - getIngredientByName | (URL: /api/v1/ingredient/name) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(ingredientResponse)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getIngredientByName | (URL: /api/v1/ingredient/name) | (Method: GET) | (status: 404)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Ingredient not found")
                            .build()
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<GeneralResponse> addIngredient(@RequestBody IngredientDTO ingredient) {
        try {
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
        catch (Exception e) {
            logger.error("response - addIngredient | (URL: /api/v1/ingredient/add) | (Method: POST) | (status: 400)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Ingredient already exists")
                            .build()
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<GeneralResponse> deleteIngredient(@RequestBody List<Long> ids) {
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
    public ResponseEntity<GeneralResponse> updateIngredient(@RequestBody IngredientDTO ingredient, @PathVariable Long id) {
        try {
            logger.info("request - updateBrand | (URL: /api/v1/ingredient/update/{id}) | (Method: PUT) | (ingredient: {}) | (id: {})", ingredient, id);
            ingredientService.updateIngredient(ingredient, id);
            logger.info("response - updateBrand | (URL: /api/v1/ingredient/update/{id}) | (Method: PUT) | (status: 200)");
            return ResponseEntity.status(HttpStatus.OK).body(
                    GeneralResponse.builder()
                            .success(true)
                            .message("Ingredient updated successfully")
                            .build()
            );
        }catch (Exception e) {
            logger.error("response - updateBrand | (URL: /api/v1/ingredient/update/{id}) | (Method: PUT) | (status: 400)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Ingredient not found")
                            .build()
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<GeneralResponse> getIngredientsByKeywords(@RequestParam(required = false) String keyword) {
        try {
            logger.info("request - getIngredientsByKeywords | (URL: /api/v1/ingredient/search}) | (Method: GET) | (keyword: {})", keyword);
            List<IngredientResponse> ingredients = ingredientService.searchIngredientsByName(keyword);
            logger.info("response - getIngredientsByKeywords | (URL: /api/v1/ingredient/search) | (Method: GET) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(ingredients)
                            .build()
            );
        }
        catch (Exception e) {
            logger.error("response - getIngredientsByKeywords | (URL: /api/v1/ingredient/search) | (Method: GET) | (status: 404)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Ingredient not found")
                            .build()
            );
        }
    }

    @GetMapping("/checker")
    public ResponseEntity<GeneralResponse> getIngredientsByNames(@RequestParam String names) {
        try {
            logger.info("request - getIngredientsByNames | (URL: /api/v1/ingredient/checker) | (Method: POST) | (names: {})", names);
            List<IngredientResponse> ingredients = ingredientService.getIngredientsByNames(names);
            logger.info("response - getIngredientsByNames | (URL: /api/v1/ingredient/checker) | (Method: POST) | (status: 200)");
            return ResponseEntity.ok(
                    GeneralResponse.builder()
                            .success(true)
                            .data(ingredients)
                            .build()
            );
        } catch (Exception e) {
            logger.error("response - getIngredientsByNames | (URL: /api/v1/ingredient/checker) | (Method: POST) | (status: 404)");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GeneralResponse.builder()
                            .success(false)
                            .message("Ingredients not found")
                            .build()
            );
        }
    }



}
