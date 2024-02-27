package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService
            (IngredientRepository ingredientRepository)
    {
        this.ingredientRepository = ingredientRepository;
    }

    public Object getAllIngredientWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return ingredientRepository.findAll(PageRequest.of(page, size,sort));
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public boolean addBrand(Ingredient ingredient) {
        if (ingredientRepository.findByName(ingredient.getName()).isPresent()) {
            return false;
        }
        ingredientRepository.save(ingredient);
        return true;
    }
    public boolean deleteBrand(List<Long> ids) {
        for (Long id : ids) {
            if (!ingredientRepository.existsById(id)) {
                return false;
            }
        }
        ingredientRepository.deleteAllById(ids);
        return true;
    }

    @Transactional
    public String updateBrand(Ingredient ingredient, Long id) {
        Optional<Ingredient> existingIngredientOptional = ingredientRepository.findById(id);

        if (existingIngredientOptional.isPresent()) {
            Ingredient existingIngredient = existingIngredientOptional.get();

            String updatedName = ingredient.getName();
            if (updatedName != null && !updatedName.equals(existingIngredient.getName())) {
                Optional<Object> existingIngredientByName = ingredientRepository.findByName(updatedName);
                if (existingIngredientByName.isPresent()) {
                    return "Ingredient already exists";
                }
            }

            existingIngredient.updateFrom(ingredient);
            return "Ingredient updated successfully";
        }
        return "Ingredient does not exist";
    }
}


