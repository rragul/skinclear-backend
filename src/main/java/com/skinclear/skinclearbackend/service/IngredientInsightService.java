package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientInsightRepository;
import com.skinclear.skinclearbackend.resource.IconResource;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IngredientInsightService {

    //VARIABLE
    private final IngredientInsightRepository ingredientInsightRepository;

    //CONSTRUCTOR
    public IngredientInsightService(IngredientInsightRepository ingredientInsightRepository) {
        this.ingredientInsightRepository = ingredientInsightRepository;
    }

    //FUNCTION
    public Page<IngredientInsight> getIngredientInsightWithPagination(int page , int size){
        Sort sort = Sort.by(Sort.Direction.ASC, "type", "name");
        return ingredientInsightRepository.findAll(PageRequest.of(page, size,sort));
    }

    public List<IconResource> getRandomImages(int limit) {
        List<IngredientInsight> ingredientInsights = ingredientInsightRepository.findRandomImages(limit);
        List<IconResource> iconResources = new java.util.ArrayList<>();
        for (IngredientInsight ingredientInsight : ingredientInsights) {
            IconResource iconResource = new IconResource();
            iconResource.setName(ingredientInsight.getName());
            iconResource.setImage(ingredientInsight.getImage());
            iconResources.add(iconResource);
        }
        return iconResources;
    }

    public void addIngredientInsight(IngredientInsight ingredientInsight){
        ingredientInsightRepository.findByName(ingredientInsight.getName()).ifPresent(existingInsight -> {
            throw new IllegalStateException("Ingredient Insight already exists");
        });
        ingredientInsightRepository.save(ingredientInsight);
    }

    @Transactional
    public void updateIngredientInsight(IngredientInsight ingredientInsight, Long id) {
        IngredientInsight existingInsight = ingredientInsightRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Ingredient Insight does not exist"));

        String updatedName = ingredientInsight.getName();
        if (updatedName != null && !updatedName.equals(existingInsight.getName())) {
            ingredientInsightRepository.findByName(updatedName).ifPresent(insight -> {
                throw new RuntimeException("Ingredient Insight with name " + updatedName + " already exists");
            });
        }
        IngredientInsight updatedInsight = new IngredientInsight();
        updatedInsight.setId(id);
        updatedInsight.setName(updatedName);
        updatedInsight.setType(ingredientInsight.getType());
        updatedInsight.setImage(ingredientInsight.getImage());
        updatedInsight.setShortDescription(ingredientInsight.getShortDescription());
        updatedInsight.setDescription(ingredientInsight.getDescription());
        updatedInsight.setConcernIngredients(ingredientInsight.getConcernIngredients());
        updatedInsight.setBenefitsIngredients(ingredientInsight.getBenefitsIngredients());
        updatedInsight.setWhatItIsIngredients(ingredientInsight.getWhatItIsIngredients());
        ingredientInsightRepository.save(updatedInsight);
    }

    @Transactional
    public void deleteIngredientInsight(List<Long> ids) {
        ingredientInsightRepository.deleteAllById(ids);
    }

    public List<IngredientInsight> getIngredientsByType(String type) {
        return ingredientInsightRepository.findByTypeIgnoreCase(type);
    }

    public Set<IngredientInsight> getIngredientsByIds(List<Long> ids){
        return new HashSet<>(ingredientInsightRepository.findAllById(ids));
    }
}

