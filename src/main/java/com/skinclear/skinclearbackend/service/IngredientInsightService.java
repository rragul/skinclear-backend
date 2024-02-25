package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientInsightRepository;
import com.skinclear.skinclearbackend.resource.IconResource;
import com.skinclear.skinclearbackend.resource.IngredientInsightsResponseResource;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    public String updateIngredientInsight(IngredientInsight ingredientInsight, Long id) {
        Optional<IngredientInsight> existingInsightOptional = ingredientInsightRepository.findById(id);

        if (existingInsightOptional.isPresent()) {
            IngredientInsight existingInsight = existingInsightOptional.get();

            String updatedName = ingredientInsight.getName();
            if (updatedName != null && !updatedName.equals(existingInsight.getName())) {
                Optional<IngredientInsight> existingInsightByName = ingredientInsightRepository.findByName(updatedName);
                if (existingInsightByName.isPresent()) {
                    return "Ingredient Insight already exists";
                }
            }

            existingInsight.updateFrom(ingredientInsight);
            return "Ingredient Insight updated successfully";
        }
        return "Ingredient Insight does not exist";
    }


    public boolean deleteIngredientInsight(List<Long> ids) {
        for (Long id : ids) {
            if(ingredientInsightRepository.findById(id).isEmpty()){
                return false;
            }
        }
        ingredientInsightRepository.deleteAll(ingredientInsightRepository.findAllById(ids));
        return true;
    }
}
