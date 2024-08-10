package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.IngredientDTO;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.IngredientResponse;
import com.skinclear.skinclearbackend.service.IngredientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllIngredientsWithPagination() {
        Page<IngredientResponse> ingredients = new PageImpl<>(List.of(new IngredientResponse()));
        when(ingredientService.getAllIngredientWithPagination(anyInt(), anyInt())).thenReturn(ingredients);

        ResponseEntity<GeneralResponse> response = ingredientController.getAllIngredientsWithPagination(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).getAllIngredientWithPagination(0, 10);
    }

    @Test
    void testGetIngredientById() {
        IngredientResponse ingredientResponse = new IngredientResponse();
        when(ingredientService.getIngredientResponseById(anyLong())).thenReturn(ingredientResponse);

        ResponseEntity<GeneralResponse> response = ingredientController.getIngredientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).getIngredientResponseById(1L);
    }

    @Test
    void testGetIngredientByName() {
        IngredientResponse ingredientResponse = new IngredientResponse();
        when(ingredientService.getIngredientResponseByName(anyString())).thenReturn(ingredientResponse);

        ResponseEntity<GeneralResponse> response = ingredientController.getIngredientByName("ingredient");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).getIngredientResponseByName("ingredient");
    }

    @Test
    void testAddIngredient() {
        doNothing().when(ingredientService).addIngredient(any(IngredientDTO.class));

        IngredientDTO ingredientDTO = new IngredientDTO();
        ResponseEntity<GeneralResponse> response = ingredientController.addIngredient(ingredientDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).addIngredient(ingredientDTO);
    }

    @Test
    void testDeleteIngredient() {
        doNothing().when(ingredientService).deleteIngredient(anyList());

        List<Long> ids = Arrays.asList(1L, 2L);
        ResponseEntity<GeneralResponse> response = ingredientController.deleteIngredient(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).deleteIngredient(ids);
    }

    @Test
    void testUpdateIngredient() {
        doNothing().when(ingredientService).updateIngredient(any(IngredientDTO.class), anyLong());

        IngredientDTO ingredientDTO = new IngredientDTO();
        ResponseEntity<GeneralResponse> response = ingredientController.updateIngredient(ingredientDTO, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).updateIngredient(ingredientDTO, 1L);
    }

    @Test
    void testGetIngredientsByKeywords() {
        when(ingredientService.searchIngredientsByName(anyString())).thenReturn(List.of(new IngredientResponse()));

        ResponseEntity<GeneralResponse> response = ingredientController.getIngredientsByKeywords("keyword");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).searchIngredientsByName("keyword");
    }

    @Test
    void testGetIngredientsByNames() {
        when(ingredientService.getIngredientsByNames(anyString())).thenReturn(List.of(new IngredientResponse()));

        ResponseEntity<GeneralResponse> response = ingredientController.getIngredientsByNames("name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientService, times(1)).getIngredientsByNames("name");
    }
}
