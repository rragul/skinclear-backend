package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.IngredientDTO;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private  final  IngredientInsightService ingredientInsightService;

    public IngredientService
            (IngredientRepository ingredientRepository, IngredientInsightService ingredientInsightService)
    {
        this.ingredientRepository = ingredientRepository;
        this.ingredientInsightService = ingredientInsightService;
    }

    public Object getAllIngredientWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return ingredientRepository.findAll(PageRequest.of(page, size,sort));
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found for id: " + id));
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

    public List<Ingredient> searchIngredientsByName(String keyword) {
        return ingredientRepository.findIngredientsByName(keyword);
    }
}


