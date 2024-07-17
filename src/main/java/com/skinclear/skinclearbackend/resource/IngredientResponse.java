package com.skinclear.skinclearbackend.resource;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {
    private Long id;
    private String name;
    private String whatItDoes;
    private String otherNames;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    private String explain;
    private List<IngredientInsight> whatItIs;
    private List<IngredientInsight> benefits;
    private List<IngredientInsight> concern;
    private int productCount;
}
