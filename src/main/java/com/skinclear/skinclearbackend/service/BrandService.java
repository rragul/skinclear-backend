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

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    public boolean addBrand(Brand brand) {
        if (brandRepository.findByName(brand.getName()).isPresent()) {
            return false;
        }
        brandRepository.save(brand);
        return true;
    }

    @Transactional
    public String updateBrand(Brand brand, Long id) {
        Optional<Brand> existingBrandOptional = brandRepository.findById(id);

        if (existingBrandOptional.isPresent()) {
            Brand existingBrand = existingBrandOptional.get();

            String updatedName = brand.getName();
            if (updatedName != null && !updatedName.equals(existingBrand.getName())) {
                Optional<Object> existingBrandByName = brandRepository.findByName(updatedName);
                if (existingBrandByName.isPresent()) {
                    return "Brand already exists";
                }
            }

            existingBrand.updateFrom(brand);
            return "Brand updated successfully";
        }
        return "Brand does not exist";
    }



    public boolean deleteBrand(List<Long> ids) {
        for (Long id : ids) {
            if (!brandRepository.existsById(id)) {
                return false;
            }
        }
        brandRepository.deleteAllById(ids);
        return true;
    }

    public Object getAllBrandWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return brandRepository.findAll(PageRequest.of(page, size,sort));
    }

    public List<Brand> searchBrandsByName(String searchKeyword) {
        return brandRepository.findTop20ByNameContainingIgnoreCase(searchKeyword);
    }
}
