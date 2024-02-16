package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientInsightRepository;
import com.skinclear.skinclearbackend.resource.IconResource;
import com.skinclear.skinclearbackend.resource.IngredientInsightsResponseResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientInsightService {

    //VARIABLE
    private final IngredientInsightRepository ingredientInsightRepository;

    //CONSTRUCTOR
    public IngredientInsightService(IngredientInsightRepository ingredientInsightRepository) {
        this.ingredientInsightRepository = ingredientInsightRepository;
    }

    //FUNCTION
    public IngredientInsightsResponseResource getIngredientInsightWithPagination(int page , int size){
        IngredientInsightsResponseResource response = new IngredientInsightsResponseResource();

        response.setTotalIngredientInsights(ingredientInsightRepository.count());

        Page<IngredientInsight> ingredientInsightPage = ingredientInsightRepository.findAll(PageRequest.of(page, size));
        response.setIngredientInsightList(ingredientInsightPage.getContent());

        return response;
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

    public boolean addIngredientInsight(IngredientInsight ingredientInsight){
        if(ingredientInsightRepository.findByName(ingredientInsight.getName()).isPresent()){
            return false;
        }
        ingredientInsightRepository.save(ingredientInsight);
        return true;
    }


    public List<IngredientInsight> getSelectedIngredientInsight(List<Long> selectedIdList){
        return ingredientInsightRepository.findIngredientInsightByIds(selectedIdList);
    }

    public List<String> getSelectedIngredientInsightImages(List<Long> selectedIdList){
        return ingredientInsightRepository.findIngredientInsightImageByIds(selectedIdList);
    }
}
