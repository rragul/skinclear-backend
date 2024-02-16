package com.skinclear.skinclearbackend.resource;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.service.IngredientService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientInsightsResponseResource {
    private long totalIngredientInsights;
    private List<IngredientInsight> ingredientInsightList;
}
