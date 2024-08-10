package com.skinclear.skinclearbackend.controller;

import com.skinclear.skinclearbackend.dto.BrandDTO;
import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.resource.GeneralResponse;
import com.skinclear.skinclearbackend.service.BrandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BrandControllerTest {

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandController brandController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBrandsWithPagination() {
        int page = 0;
        int size = 10;
        Object expectedResponse = new Object();

        when(brandService.getAllBrandWithPagination(page, size)).thenReturn(expectedResponse);

        ResponseEntity<GeneralResponse> response = brandController.getAllBrandsWithPagination(page, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(expectedResponse, response.getBody().getData());
        verify(brandService, times(1)).getAllBrandWithPagination(page, size);
    }

    @Test
    void testGetAllBrands() {
        int page = 0;
        int size = 10;
        boolean isCrueltyFree = true;
        String country = "USA";
        Object expectedResponse = new Object();

        when(brandService.getAllBrandWithPagination(page, size, isCrueltyFree, country)).thenReturn(expectedResponse);

        ResponseEntity<GeneralResponse> response = brandController.getAllBrands(page, size, isCrueltyFree, country);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(expectedResponse, response.getBody().getData());
        verify(brandService, times(1)).getAllBrandWithPagination(page, size, isCrueltyFree, country);
    }

    @Test
    void testGetBrandById() {
        Long id = 1L;
        Brand expectedBrand = new Brand();

        when(brandService.getBrandById(id)).thenReturn(expectedBrand);

        ResponseEntity<GeneralResponse> response = brandController.getBrandById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(expectedBrand, response.getBody().getData());
        verify(brandService, times(1)).getBrandById(id);
    }

    @Test
    void testGetBrandsByKeywords() {
        String keyword = "test";
        List<Brand> expectedBrands = Arrays.asList(new Brand(), new Brand());

        when(brandService.searchBrandsByName(keyword)).thenReturn(expectedBrands);

        ResponseEntity<GeneralResponse> response = brandController.getBrandsByKeywords(keyword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals(expectedBrands, response.getBody().getData());
        verify(brandService, times(1)).searchBrandsByName(keyword);
    }

    @Test
    void testAddBrand() {
        BrandDTO brandDTO = new BrandDTO();

        ResponseEntity<GeneralResponse> response = brandController.addBrand(brandDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Brand added successfully", response.getBody().getMessage());
        verify(brandService, times(1)).addBrand(brandDTO);
    }

    @Test
    void testUpdateBrand() {
        Long id = 1L;
        Brand brand = new Brand();

        ResponseEntity<GeneralResponse> response = brandController.updateBrand(brand, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Brand updated successfully", response.getBody().getMessage());
        verify(brandService, times(1)).updateBrand(brand, id);
    }

    @Test
    void testDeleteBrand() {
        List<Long> ids = Arrays.asList(1L, 2L);

        ResponseEntity<GeneralResponse> response = brandController.deleteBrand(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().isSuccess());
        assertEquals("Brand deleted successfully", response.getBody().getMessage());
        verify(brandService, times(1)).deleteBrand(ids);
    }
}
