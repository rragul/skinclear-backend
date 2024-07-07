package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.IngredientInsightDTO;
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

    public void addIngredientInsight(IngredientInsightDTO ingredientInsight){
        ingredientInsightRepository.findByName(ingredientInsight.getName()).ifPresent(existingInsight -> {
            throw new RuntimeException("Ingredient Insight already exists");
        });
        IngredientInsight newInsight = createIngredientInsightFromDTO(ingredientInsight);
        ingredientInsightRepository.save(newInsight);
    }

    private IngredientInsight createIngredientInsightFromDTO(IngredientInsightDTO ingredientInsight) {
        return new IngredientInsight(
                ingredientInsight.getName(),
                ingredientInsight.getType(),
                ingredientInsight.getImage(),
                ingredientInsight.getShortDescription(),
                ingredientInsight.getDescription()
        );
    }

    @Transactional
    public void updateIngredientInsight(IngredientInsightDTO ingredientInsightDTO, Long id) {
        IngredientInsight existingInsight = ingredientInsightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient Insight does not exist"));

        String updatedName = ingredientInsightDTO.getName();
        if (updatedName != null && !updatedName.equals(existingInsight.getName())) {
            ingredientInsightRepository.findByName(updatedName).ifPresent(insight -> {
                throw new RuntimeException("Ingredient Insight with name " + updatedName + " already exists");
            });
        }
        IngredientInsight updatedInsight = createIngredientInsightFromDTO(ingredientInsightDTO);
        updatedInsight.setId(id);
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

