package com.skinclear.skinclearbackend.resource;

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
    private String name;
    private String whatItDoes;
    private String otherNames;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    private String explain;
    private List<String> whatItIs;
    private List<String> benefits;
    private List<String> concern;
    private int productCount;
}
