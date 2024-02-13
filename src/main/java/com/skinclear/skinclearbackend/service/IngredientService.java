package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import com.skinclear.skinclearbackend.resource.IngredientsDictionaryResponseResource;
import com.skinclear.skinclearbackend.resource.IngredientDictionaryResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public IngredientsDictionaryResponseResource getStructuredIngredients(int page, int size) {
        List<IngredientDictionaryResource> ingredientsResources = getIngredientsWithPagination(page, size);
        IngredientsDictionaryResponseResource ingredientResponseResource = new IngredientsDictionaryResponseResource();
        ingredientResponseResource.setIngredients(ingredientsResources);
        ingredientResponseResource.setTotalIngredients(ingredientRepository.count());
        return ingredientResponseResource;
    }


    public List<IngredientDictionaryResource> getIngredientsWithPagination(int page, int size) {
        Page<Ingredient> ingredients = ingredientRepository.findAll(PageRequest.of(page, size));
        List<IngredientDictionaryResource> ingredientsResources =  new ArrayList<>();
        for (Ingredient ingredient : ingredients.getContent()) {
            IngredientDictionaryResource ingredientsResource = new IngredientDictionaryResource();
            ingredientsResource.setId(ingredient.getId());
            ingredientsResource.setName(ingredient.getName());
            //TODO: need to change product count
            ingredientsResource.setProductCount(10);
            if (ingredient.getLikeCount() == null) {
                ingredient.setLikeCount(0);
            }
            if (ingredient.getDislikeCount() == null) {
                ingredient.setDislikeCount(0);
            }
            ingredientsResource.setLikeCount(ingredient.getLikeCount());
            ingredientsResource.setDislikeCount(ingredient.getDislikeCount());
            List<Long> benefitsId = getLongIds(ingredient.getBenefitsId());
            List<Long> concernsId = getLongIds(ingredient.getConcernId());
            List<Long> typesId = getLongIds(ingredient.getTypeId());
            List<Long> ids = new ArrayList<>();
            ids.addAll(benefitsId);
            ids.addAll(concernsId);
            ids.addAll(typesId);
            ingredientsResource.setImages(ingredientInsightService.getSelectedIngredientInsightImages(ids));
            ingredientsResource.setWhatItDoes(getStringList(ingredient.getWhatItDoes()));
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


