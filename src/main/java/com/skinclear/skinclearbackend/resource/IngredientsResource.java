package com.skinclear.skinclearbackend.resource;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsResource {
    private Long id;
    private String name;
    private List<IngredientInsight> typelist;
    private List<String> whatItDoes;
    private List<IngredientInsight>  benefitsList;
    private String otherNames;
    private List<IngredientInsight>  concernList;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    private String explain;
}
