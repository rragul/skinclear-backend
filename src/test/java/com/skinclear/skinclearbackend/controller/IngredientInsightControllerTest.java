package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.IngredientInsightDTO;
import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.resource.IconResource;
import com.skinclear.skinclearbackend.service.IngredientInsightService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class IngredientInsightControllerTest {

    @Mock
    private IngredientInsightService ingredientInsightService;

    @InjectMocks
    private IngredientInsightController ingredientInsightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIngredientInsights() {
        Page<IngredientInsight> ingredientInsightsPage = new PageImpl<>(Collections.emptyList());
        when(ingredientInsightService.getIngredientInsightWithPagination(anyInt(), anyInt())).thenReturn(ingredientInsightsPage);

        ResponseEntity<GeneralResponse> response = ingredientInsightController.getIngredientInsights(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientInsightService, times(1)).getIngredientInsightWithPagination(0, 10);
    }

    @Test
    void testGetIngredientInsightsByType() {
        List<IngredientInsight> ingredientInsights = Arrays.asList(new IngredientInsight(), new IngredientInsight());
        when(ingredientInsightService.getIngredientsByType(anyString())).thenReturn(ingredientInsights);

        ResponseEntity<GeneralResponse> response = ingredientInsightController.getIngredientInsightsByType("type");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientInsightService, times(1)).getIngredientsByType("type");
    }

    @Test
    void testGetRandomImages() {
        List<IconResource> iconResources = Arrays.asList(new IconResource(), new IconResource());
        when(ingredientInsightService.getRandomImages(anyInt())).thenReturn(iconResources);

        ResponseEntity<GeneralResponse> response = ingredientInsightController.getRandomImages(10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientInsightService, times(1)).getRandomImages(10);
    }

    @Test
    void testAddIngredientInsights() {
        doNothing().when(ingredientInsightService).addIngredientInsight(any(IngredientInsightDTO.class));

        IngredientInsightDTO ingredientInsightDTO = new IngredientInsightDTO();
        ResponseEntity<GeneralResponse> response = ingredientInsightController.addIngredientInsights(ingredientInsightDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientInsightService, times(1)).addIngredientInsight(ingredientInsightDTO);
    }

    @Test
    void testUpdateIngredientInsights() {
        doNothing().when(ingredientInsightService).updateIngredientInsight(any(IngredientInsightDTO.class), anyLong());

        IngredientInsightDTO ingredientInsightDTO = new IngredientInsightDTO();
        ResponseEntity<GeneralResponse> response = ingredientInsightController.updateIngredientInsights(ingredientInsightDTO, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientInsightService, times(1)).updateIngredientInsight(ingredientInsightDTO, 1L);
    }

    @Test
    void testDeleteIngredientInsights() {
        doNothing().when(ingredientInsightService).deleteIngredientInsight(anyList());

        List<Long> ids = Arrays.asList(1L, 2L);
        ResponseEntity<GeneralResponse> response = ingredientInsightController.deleteIngredientInsights(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        verify(ingredientInsightService, times(1)).deleteIngredientInsight(ids);
    }
}
