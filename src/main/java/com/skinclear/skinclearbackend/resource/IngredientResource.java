package com.skinclear.skinclearbackend.resource;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResource {
    private String name;
    private int productCount;
    private List<IngredientInsight> whatItIs;
    private List<IngredientInsight> benefits;
    private List<IngredientInsight> concerns;
    private String explain;
    private String alsoKnownAs;
    private String rarity;
    private List<String> whatItDoes;
    private List<Product> products;
}
