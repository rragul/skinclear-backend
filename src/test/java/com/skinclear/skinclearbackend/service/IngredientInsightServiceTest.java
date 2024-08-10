package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.IngredientInsightDTO;
import com.skinclear.skinclearbackend.entity.IngredientInsight;
import com.skinclear.skinclearbackend.repository.IngredientInsightRepository;
import com.skinclear.skinclearbackend.resource.IconResource;
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

class IngredientInsightServiceTest {

    @Mock
    private IngredientInsightRepository ingredientInsightRepository;

    @InjectMocks
    private IngredientInsightService ingredientInsightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIngredientInsightWithPagination() {
        IngredientInsight insight1 = new IngredientInsight("Insight1", "Type1", "Image1", "ShortDesc1", "Desc1");
        IngredientInsight insight2 = new IngredientInsight("Insight2", "Type1", "Image2", "ShortDesc2", "Desc2");

        List<IngredientInsight> insights = Arrays.asList(insight1, insight2);
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "type", "name"));
        Page<IngredientInsight> insightPage = new PageImpl<>(insights, pageRequest, insights.size());

        when(ingredientInsightRepository.findAll(any(PageRequest.class))).thenReturn(insightPage);

        Page<IngredientInsight> result = ingredientInsightService.getIngredientInsightWithPagination(0, 2);

        assertEquals(2, result.getTotalElements());
        assertEquals(insight1.getName(), result.getContent().get(0).getName());
        assertEquals(insight2.getName(), result.getContent().get(1).getName());
    }

    @Test
    void testGetRandomImages() {
        IngredientInsight insight = new IngredientInsight("Insight1", "Type1", "Image1", "ShortDesc1", "Desc1");
        List<IngredientInsight> insights = Collections.singletonList(insight);

        when(ingredientInsightRepository.findRandomImages(1)).thenReturn(insights);

        List<IconResource> result = ingredientInsightService.getRandomImages(1);

        assertEquals(1, result.size());
        assertEquals(insight.getName(), result.get(0).getName());
        assertEquals(insight.getImage(), result.get(0).getImage());
    }

    @Test
    void testAddIngredientInsight() {
        IngredientInsightDTO insightDTO = new IngredientInsightDTO("Insight1", "Type1", "Image1", "ShortDesc1", "Desc1");

        when(ingredientInsightRepository.findByName(insightDTO.getName())).thenReturn(Optional.empty());

        ingredientInsightService.addIngredientInsight(insightDTO);

        verify(ingredientInsightRepository, times(1)).save(any(IngredientInsight.class));
    }

    @Test
    void testUpdateIngredientInsight() {
        IngredientInsightDTO insightDTO = new IngredientInsightDTO("UpdatedName", "Type1", "Image1", "ShortDesc1", "Desc1");
        IngredientInsight existingInsight = new IngredientInsight("OldName", "Type1", "Image1", "ShortDesc1", "Desc1");

        when(ingredientInsightRepository.findById(1L)).thenReturn(Optional.of(existingInsight));
        when(ingredientInsightRepository.findByName(insightDTO.getName())).thenReturn(Optional.empty());

        ingredientInsightService.updateIngredientInsight(insightDTO, 1L);

        verify(ingredientInsightRepository, times(1)).save(any(IngredientInsight.class));
    }

    @Test
    void testDeleteIngredientInsight() {
        List<Long> ids = Arrays.asList(1L, 2L);

        ingredientInsightService.deleteIngredientInsight(ids);

        verify(ingredientInsightRepository, times(1)).deleteAllById(ids);
    }

    @Test
    void testGetIngredientsByType() {
        String type = "Type1";
        IngredientInsight insight = new IngredientInsight("Insight1", type, "Image1", "ShortDesc1", "Desc1");
        List<IngredientInsight> insights = Collections.singletonList(insight);

        when(ingredientInsightRepository.findByTypeIgnoreCase(type)).thenReturn(insights);

        List<IngredientInsight> result = ingredientInsightService.getIngredientsByType(type);

        assertEquals(1, result.size());
        assertEquals(type, result.get(0).getType());
    }

    @Test
    void testGetIngredientsByIds() {
        IngredientInsight insight = new IngredientInsight("Insight1", "Type1", "Image1", "ShortDesc1", "Desc1");
        List<IngredientInsight> insights = Collections.singletonList(insight);
        List<Long> ids = Collections.singletonList(1L);

        when(ingredientInsightRepository.findAllById(ids)).thenReturn(insights);

        Set<IngredientInsight> result = ingredientInsightService.getIngredientsByIds(ids);

        assertEquals(1, result.size());
        assertTrue(result.contains(insight));
    }
}
