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
        return ingredientRepository.findById(id).orElse(null);
    }

    public boolean addIngredient(IngredientDTO ingredientDTO) {
        if (ingredientRepository.findByName(ingredientDTO.getName()).isPresent()) {
            return false;
        }

        ingredientRepository.save(createNewIngredientFromIngredientDTO(ingredientDTO));
        return true;
    }

    public boolean deleteIngredient(List<Long> ids) {
        for (Long id : ids) {
            if (!ingredientRepository.existsById(id)) {
                return false;
            }
        }
        ingredientRepository.deleteAllById(ids);
        return true;
    }

    @Transactional
    public String updateIngredient(IngredientDTO ingredientDTO, Long id) {
        Optional<Ingredient> existingIngredientOptional = ingredientRepository.findById(id);

        if (existingIngredientOptional.isPresent()) {
            Ingredient existingIngredient = existingIngredientOptional.get();

            String updatedName = ingredientDTO.getName();
            if (updatedName != null && !updatedName.equals(existingIngredient.getName())) {
                Optional<Object> existingIngredientByName = ingredientRepository.findByName(updatedName);
                if (existingIngredientByName.isPresent()) {
                    return "Ingredient already exists";
                }
            }
            Ingredient ingredient = createNewIngredientFromIngredientDTO(ingredientDTO);
            ingredient.setId(id);
            ingredientRepository.save(ingredient);
           // existingIngredient.updateFrom(createNewIngredientFromIngredientDTO(ingredientDTO));
            return "Ingredient updated successfully";
        }
        return "Ingredient does not exist";
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
}


