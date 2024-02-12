package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import com.skinclear.skinclearbackend.resource.IngredientsResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<IngredientsResource> getIngredients() {
       List<Ingredient> ingredients = ingredientRepository.findAll();
       List<IngredientsResource> ingredientsResources =  new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            IngredientsResource ingredientsResource = new IngredientsResource();

            List<IngredientInsight> typelist =
                    ingredientInsightService.getSelectedIngredientInsight(getLongIds(ingredient.getTypeId()));
            List<IngredientInsight> benefitsList =
                    ingredientInsightService.getSelectedIngredientInsight(getLongIds(ingredient.getBenefitsId()));
            List<IngredientInsight> concernList =
                    ingredientInsightService.getSelectedIngredientInsight(getLongIds(ingredient.getConcernId()));

            ingredientsResource.setId(ingredient.getId());
            ingredientsResource.setName(ingredient.getName());
            ingredientsResource.setTypelist(typelist);
            ingredientsResource.setWhatItDoes(getStringList(ingredient.getWhatItDoes()));
            ingredientsResource.setBenefitsList(benefitsList);
            ingredientsResource.setOtherNames(ingredient.getOtherNames());
            ingredientsResource.setConcernList(concernList);
            ingredientsResource.setRarity(ingredient.getRarity());
            ingredientsResource.setLikeCount(ingredient.getLikeCount());
            ingredientsResource.setDislikeCount(ingredient.getDislikeCount());
            ingredientsResource.setExplain(ingredient.getExplain());
            ingredientsResources.add(ingredientsResource);
        }

       return ingredientsResources;
    }

    public  List<Ingredient> getUnstructuredIngredients(){
        return ingredientRepository.findAll();
    }

    public List<Long> getLongIds(String idString) {
        List<Long> longIds = new ArrayList<>();
        if (idString != null && !idString.isEmpty()) {

            String[] ids = idString.split(",");

            for (String id : ids) {
                longIds.add(Long.valueOf(id.trim())); // Trim to remove leading/trailing whitespace
            }
        }
        return longIds;
    }

    public List<String> getStringList(String combinedString) {
        List<String> stringList = new ArrayList<>();
        if (combinedString != null && !combinedString.isEmpty()) {

            String[] strings = combinedString.split(",");

            for (String string : strings) {
                stringList.add(string.trim()); // Trim to remove leading/trailing whitespace
            }
        }
        return stringList;
    }
}


