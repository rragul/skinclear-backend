package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.dto.BrandDTO;
import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.repository.BrandRepository;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import com.skinclear.skinclearbackend.resource.BrandResources;
import com.skinclear.skinclearbackend.resource.PagnatedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBrandWithPagination() {
        // Arrange
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("BrandA");
        Page<Brand> page = new PageImpl<>(Collections.singletonList(brand));
        when(brandRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")))).thenReturn(page);

        // Act
        Object result = brandService.getAllBrandWithPagination(0, 10);

        // Assert
        assertEquals(page, result);
        verify(brandRepository, times(1)).findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    void testGetAllBrandWithPagination_FilteredByCrueltyFreeAndCountry() {
        // Arrange
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("BrandA");
        Page<Brand> page = new PageImpl<>(Collections.singletonList(brand));
        when(brandRepository.findByIsCrueltyFreeAndCountry(true, "USA", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")))).thenReturn(page);

        // Act
        PagnatedResponse<BrandResources> result = (PagnatedResponse<BrandResources>) brandService.getAllBrandWithPagination(0, 10, true, "USA");

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(brandRepository, times(1)).findByIsCrueltyFreeAndCountry(true, "USA", PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));
    }

    @Test
    void testGetBrandById_Success() {
        // Arrange
        Brand brand = new Brand();
        brand.setId(1L);
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        // Act
        Brand result = brandService.getBrandById(1L);

        // Assert
        assertEquals(brand, result);
        verify(brandRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBrandById_Failure() {
        // Arrange
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> brandService.getBrandById(1L));
        assertEquals("Brand does not exist", exception.getMessage());
        verify(brandRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBrand_Success() {
        // Arrange
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("BrandA");
        when(brandRepository.findByName("BrandA")).thenReturn(Optional.empty());

        // Act
        brandService.addBrand(brandDTO);

        // Assert
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void testAddBrand_Failure() {
        // Arrange
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("BrandA");
        when(brandRepository.findByName("BrandA")).thenReturn(Optional.of(new Brand()));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> brandService.addBrand(brandDTO));
        assertEquals("Brand already exists", exception.getMessage());
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void testUpdateBrand_Success() {
        // Arrange
        Brand brand = new Brand();
        brand.setName("BrandA");
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.findByName("BrandA")).thenReturn(Optional.empty());

        // Act
        brandService.updateBrand(brand, 1L);

        // Assert
        verify(brandRepository, times(1)).save(any(Brand.class));
    }

    @Test
    void testUpdateBrand_Failure() {
        // Arrange
        Brand brand = new Brand();
        brand.setName("BrandA");
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> brandService.updateBrand(brand, 1L));
        assertEquals("Brand does not exist", exception.getMessage());
        verify(brandRepository, never()).save(any(Brand.class));
    }

    @Test
    void testDeleteBrand_Success() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        when(brandRepository.existsById(1L)).thenReturn(true);
        when(brandRepository.existsById(2L)).thenReturn(true);

        // Act
        brandService.deleteBrand(ids);

        // Assert
        verify(brandRepository, times(1)).deleteAllById(ids);
    }

    @Test
    void testDeleteBrand_Failure() {
        // Arrange
        List<Long> ids = Collections.singletonList(1L);
        when(brandRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> brandService.deleteBrand(ids));
        assertEquals("Brand does not exist", exception.getMessage());
        verify(brandRepository, never()).deleteAllById(ids);
    }

    @Test
    void testSearchBrandsByName_Success() {
        // Arrange
        Brand brand = new Brand();
        brand.setName("BrandA");
        when(brandRepository.findTop10ByNameStartingWithIgnoreCase("Brand")).thenReturn(Collections.singletonList(brand));

        // Act
        List<Brand> result = brandService.searchBrandsByName("Brand");

        // Assert
        assertEquals(1, result.size());
        assertEquals("BrandA", result.get(0).getName());
        verify(brandRepository, times(1)).findTop10ByNameStartingWithIgnoreCase("Brand");
    }

    @Test
    void testSearchBrandsByName_Empty() {
        // Arrange
        when(brandRepository.findFirst10()).thenReturn(Collections.emptyList());

        // Act
        List<Brand> result = brandService.searchBrandsByName("");

        // Assert
        assertEquals(0, result.size());
        verify(brandRepository, times(1)).findFirst10();
    }
}
