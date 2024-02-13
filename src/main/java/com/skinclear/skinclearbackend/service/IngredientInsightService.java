package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientInsightRepository;
import com.skinclear.skinclearbackend.resource.IconResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientInsightService {

    private final IngredientInsightRepository ingredientInsightRepository;

    public IngredientInsightService(IngredientInsightRepository ingredientInsightRepository) {
        this.ingredientInsightRepository = ingredientInsightRepository;
    }

    public List<IngredientInsight> getIngredientInsights() {
        return ingredientInsightRepository.findAll();
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

    public  List<IngredientInsight> getSelectedIngredientInsight(List<Long> selectedIdList){
        return  ingredientInsightRepository.findIngredientInsightByIds(selectedIdList);
    }
    public  List<String> getSelectedIngredientInsightImages(List<Long> selectedIdList){
        return  ingredientInsightRepository.findIngredientInsightImageByIds(selectedIdList);
    }
}
