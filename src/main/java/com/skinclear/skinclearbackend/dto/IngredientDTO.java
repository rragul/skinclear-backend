package com.skinclear.skinclearbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDTO {
    private String name;
    private String whatItDoes;
    private Long[] benefitsIDs;
    private String otherNames;
    private Long[] concernIDs;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    private String explain;
    private Long[] whatItIsIDs;
}
