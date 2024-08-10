package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.IngredientDTO;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientRepository;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import com.skinclear.skinclearbackend.resource.IngredientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private IngredientInsightService ingredientInsightService;

    @InjectMocks
    private IngredientService ingredientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllIngredientWithPagination() {
        Ingredient ingredient1 = new Ingredient("Ingredient1", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        Ingredient ingredient2 = new Ingredient("Ingredient2", "WhatItDoes2", new HashSet<>(), "OtherNames2", new HashSet<>(), "Rarity2", 15, 10, "Explain2", new HashSet<>());

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));
        Page<Ingredient> ingredientPage = new PageImpl<>(ingredients, pageRequest, ingredients.size());

        when(ingredientRepository.findAll(any(PageRequest.class))).thenReturn(ingredientPage);
        when(productRepository.countProductsByIngredientId(anyLong())).thenReturn(2);

        Page<IngredientResponse> result = ingredientService.getAllIngredientWithPagination(0, 2);

        assertEquals(2, result.getTotalElements());
        assertEquals(ingredient1.getName(), result.getContent().get(0).getName());
        assertEquals(ingredient2.getName(), result.getContent().get(1).getName());
    }

    @Test
    void testGetIngredientById() {
        Ingredient ingredient = new Ingredient("Ingredient1", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        ingredient.setId(1L);

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(ingredient));

        Ingredient result = ingredientService.getIngredientById(1L);

        assertNotNull(result);
        assertEquals(ingredient.getName(), result.getName());
    }

    @Test
    void testAddIngredient() {
        IngredientDTO ingredientDTO = new IngredientDTO("UpdatedName", "WhatItDoes1",new Long[]{3L},
                "Rarity1", new Long[]{3L}, "test", 5, 5, "test", new Long[]{3L});

        when(ingredientRepository.findByName(ingredientDTO.getName())).thenReturn(Optional.empty());
        when(ingredientInsightService.getIngredientsByIds(anyList())).thenReturn(new HashSet<>());

        ingredientService.addIngredient(ingredientDTO);

        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void testDeleteIngredient() {
        List<Long> ids = Arrays.asList(1L, 2L);

        ingredientService.deleteIngredient(ids);

        verify(ingredientRepository, times(1)).deleteAllById(ids);
    }

    @Test
    void testUpdateIngredient() {
        IngredientDTO ingredientDTO = new IngredientDTO("UpdatedName", "WhatItDoes1",new Long[]{3L},
                "Rarity1", new Long[]{3L}, "test", 5, 5, "test", new Long[]{3L});
        Ingredient existingIngredient = new Ingredient("OldName", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        existingIngredient.setId(1L);

        when(ingredientRepository.findById(1L)).thenReturn(Optional.of(existingIngredient));
        when(ingredientRepository.findByName(ingredientDTO.getName())).thenReturn(Optional.empty());
        when(ingredientInsightService.getIngredientsByIds(anyList())).thenReturn(new HashSet<>());

        ingredientService.updateIngredient(ingredientDTO, 1L);

        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void testSearchIngredientsByName() {
        Ingredient ingredient = new Ingredient("Ingredient1", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        List<Ingredient> ingredients = Collections.singletonList(ingredient);

        when(ingredientRepository.findTop10ByNameStartingWithIgnoreCase("Ingr")).thenReturn(ingredients);
        when(productRepository.countProductsByIngredientId(anyLong())).thenReturn(2);

        List<IngredientResponse> result = ingredientService.searchIngredientsByName("Ingr");

        assertEquals(1, result.size());
        assertEquals(ingredient.getName(), result.get(0).getName());
    }

    @Test
    void testGetIngredientsByIds() {
        Ingredient ingredient = new Ingredient("Ingredient1", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        List<Ingredient> ingredients = Collections.singletonList(ingredient);

        when(ingredientRepository.findAllById(anyList())).thenReturn(ingredients);

        List<Ingredient> result = ingredientService.getIngredientsByIds(Arrays.asList(1L, 2L));

        assertEquals(1, result.size());
        assertEquals(ingredient.getName(), result.get(0).getName());
    }

    @Test
    void testGetIngredientResponseByName() {
        Ingredient ingredient = new Ingredient("Ingredient1", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        ingredient.setId(1L);

        when(ingredientRepository.findByNameIgnoreCase("Ingredient1")).thenReturn(Optional.of(ingredient));
        when(productRepository.countProductsByIngredientId(anyLong())).thenReturn(2);

        IngredientResponse result = ingredientService.getIngredientResponseByName("Ingredient1");

        assertNotNull(result);
        assertEquals(ingredient.getName(), result.getName());
    }

    @Test
    void testGetIngredientsByNames() {
        Ingredient ingredient1 = new Ingredient("Ingredient1", "WhatItDoes1", new HashSet<>(), "OtherNames1", new HashSet<>(), "Rarity1", 10, 5, "Explain1", new HashSet<>());
        Ingredient ingredient2 = new Ingredient("Ingredient2", "WhatItDoes2", new HashSet<>(), "OtherNames2", new HashSet<>(), "Rarity2", 15, 10, "Explain2", new HashSet<>());

        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        when(ingredientRepository.findByNameOrOtherNames(anyList())).thenReturn(ingredients);
        when(productRepository.countProductsByIngredientId(anyLong())).thenReturn(2);

        List<IngredientResponse> result = ingredientService.getIngredientsByNames("Ingredient1,Ingredient2");

        assertEquals(2, result.size());
        assertEquals(ingredient1.getName(), result.get(0).getName());
        assertEquals(ingredient2.getName(), result.get(1).getName());
    }
}
