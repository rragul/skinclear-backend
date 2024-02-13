package com.skinclear.skinclearbackend.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDictionaryResource {
    private Long id;
    private String name;
    private int productCount;
    private int likeCount;
    private int dislikeCount;
    private List<String> images;
    private List<String> whatItDoes;
}
