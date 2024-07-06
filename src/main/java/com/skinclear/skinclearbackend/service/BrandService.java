package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.repository.BrandRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Object getAllBrandWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return brandRepository.findAll(PageRequest.of(page, size,sort));
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Brand does not exist")
        );
    }

    public void addBrand(Brand brand) {
        brandRepository.findByName(brand.getName()).ifPresent(
                (existingBrand) -> {
                    throw new RuntimeException("Brand already exists");
                }
        );
        brandRepository.save(brand);
    }

    @Transactional
    public void updateBrand(Brand brand, Long id) {
        Brand existingBrand = brandRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Brand does not exist"));

        String updatedName = brand.getName();
        if (updatedName != null && !updatedName.equals(existingBrand.getName())) {
            brandRepository.findByName(updatedName).ifPresent(
                    (existingBrandWithUpdatedName) -> {
                        throw new RuntimeException("Brand with updated name already exists");
                    }
            );
        }
        Brand updatedBrand = new Brand();
        updatedBrand.setId(id);
        updatedBrand.setName(brand.getName());
        updatedBrand.setDescription(brand.getDescription());
        updatedBrand.setCountry(brand.getCountry());
        updatedBrand.setCrueltyFree(brand.isCrueltyFree());
        brandRepository.save(updatedBrand);
    }

    public void deleteBrand(List<Long> ids) {
        for (Long id : ids) {
            if (!brandRepository.existsById(id)) {
                throw new RuntimeException("Brand does not exist");
            }
        }
        brandRepository.deleteAllById(ids);
    }

    public List<Brand> searchBrandsByName(String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return brandRepository.findFirst10();
        }
        return brandRepository.findTop10ByNameStartingWithIgnoreCase(searchKeyword);
    }
}
