package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.IngredientDTO;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import com.skinclear.skinclearbackend.resource.IngredientResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ProductRepository productRepository;
    private  final  IngredientInsightService ingredientInsightService;
    public IngredientService
            (IngredientRepository ingredientRepository, IngredientInsightService ingredientInsightService, ProductRepository productRepository)
    {
        this.ingredientRepository = ingredientRepository;
        this.ingredientInsightService = ingredientInsightService;
        this.productRepository = productRepository;
    }

    public Page<IngredientResponse> getAllIngredientWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Page<Ingredient> ingredientsPage = ingredientRepository.findAll(PageRequest.of(page, size, sort));

        return covertToIngredientResponse(ingredientsPage);
    }

    private Page<IngredientResponse> covertToIngredientResponse(Page<Ingredient> ingredientsPage) {
        return ingredientsPage.map(ingredient -> new IngredientResponse(
                ingredient.getName(),
                ingredient.getWhatItDoes(),
                ingredient.getOtherNames(),
                ingredient.getRarity(),
                ingredient.getLikeCount(),
                ingredient.getDislikeCount(),
                ingredient.getExplain(),
                ingredient.getWhatItIs().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getBenefits().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getConcern().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                getProductCount(ingredient)
        ));
    }

    private int getProductCount(Ingredient ingredient) {
        return productRepository.countProductsByIngredientId(ingredient.getId());
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found for id: " + id));
    }

    public IngredientResponse getIngredientResponseById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found for id: " + id));
        return new IngredientResponse(
                ingredient.getName(),
                ingredient.getWhatItDoes(),
                ingredient.getOtherNames(),
                ingredient.getRarity(),
                ingredient.getLikeCount(),
                ingredient.getDislikeCount(),
                ingredient.getExplain(),
                ingredient.getWhatItIs().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getBenefits().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getConcern().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                getProductCount(ingredient)
        );
    }

    public void addIngredient(IngredientDTO ingredientDTO) {
        String name = ingredientDTO.getName();
        ingredientRepository.findByName(name)
                .ifPresent(ingredient -> {
                    throw new RuntimeException("Ingredient with name: " + name + " already exists");
                });
        Ingredient ingredient = createNewIngredientFromIngredientDTO(ingredientDTO);
        ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(List<Long> ids) {
        ingredientRepository.deleteAllById(ids);
    }

    @Transactional
    public void updateIngredient(IngredientDTO ingredientDTO, Long id) {
        Ingredient existingIngredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found for id: " + id));

        String updatedName = ingredientDTO.getName();
        if (updatedName != null && !updatedName.equals(existingIngredient.getName())) {
            ingredientRepository.findByName(updatedName)
                    .ifPresent(ingredient -> {
                        throw new RuntimeException("Ingredient with name: " + updatedName + " already exists");
                    });
        }
        Ingredient ingredient = createNewIngredientFromIngredientDTO(ingredientDTO);
        ingredient.setId(id);
        ingredientRepository.save(ingredient);
    }

    private  Ingredient createNewIngredientFromIngredientDTO(IngredientDTO ingredientDTO){
        List<Long> whatItIsIds = Arrays.stream(ingredientDTO.getWhatItIsIDs()).toList();
        List<Long> benefitIds = Arrays.stream(ingredientDTO.getBenefitsIDs()).toList();
        List<Long> concernIds = Arrays.stream(ingredientDTO.getConcernIDs()).toList();

        Set<IngredientInsight> whatItIsIngredientInsight = ingredientInsightService.getIngredientsByIds(whatItIsIds);
        Set<IngredientInsight> benefitIngredientInsight = ingredientInsightService.getIngredientsByIds(benefitIds);
        Set<IngredientInsight> concernIngredientInsight = ingredientInsightService.getIngredientsByIds(concernIds);

        return new Ingredient(
                ingredientDTO.getName(),
                ingredientDTO.getWhatItDoes(),
                benefitIngredientInsight,
                ingredientDTO.getOtherNames(),
                concernIngredientInsight,
                ingredientDTO.getRarity(),
                ingredientDTO.getLikeCount(),
                ingredientDTO.getDislikeCount(),
                ingredientDTO.getExplain(),
                whatItIsIngredientInsight
        );

    }

    public List<IngredientResponse> searchIngredientsByName(String keyword) {
        List<Ingredient> ingredients = (keyword == null || keyword.isBlank())
                ? ingredientRepository.findFirst10()
                : ingredientRepository.findTop10ByNameStartingWithIgnoreCase(keyword);

        return ingredients.stream().map(ingredient -> new IngredientResponse(
                ingredient.getName(),
                ingredient.getWhatItDoes(),
                ingredient.getOtherNames(),
                ingredient.getRarity(),
                ingredient.getLikeCount(),
                ingredient.getDislikeCount(),
                ingredient.getExplain(),
                ingredient.getWhatItIs().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getBenefits().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getConcern().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                getProductCount(ingredient)
        )).collect(Collectors.toList());
    }


    public List<Ingredient> getIngredientsByIds(List<Long> ingredientIds) {
        return ingredientRepository.findAllById(ingredientIds);
    }

    public IngredientResponse getIngredientResponseByName(String name) {
        Ingredient ingredient = ingredientRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Ingredient not found with name: " + name));
        return new IngredientResponse(
                ingredient.getName(),
                ingredient.getWhatItDoes(),
                ingredient.getOtherNames(),
                ingredient.getRarity(),
                ingredient.getLikeCount(),
                ingredient.getDislikeCount(),
                ingredient.getExplain(),
                ingredient.getWhatItIs().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getBenefits().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                ingredient.getConcern().stream().map(IngredientInsight::getName).collect(Collectors.toList()),
                getProductCount(ingredient)
        );
    }
}


