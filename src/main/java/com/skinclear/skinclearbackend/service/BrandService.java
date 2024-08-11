package com.skinclear.skinclearbackend.service;

import com.skinclear.skinclearbackend.resource.PagnatedResponse;
import com.skinclear.skinclearbackend.dto.BrandDTO;
import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.repository.BrandRepository;
import com.skinclear.skinclearbackend.repository.ProductRepository;
import com.skinclear.skinclearbackend.resource.BrandResources;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    public BrandService(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    public Object getAllBrandWithPagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC,  "name");
        return brandRepository.findAll(PageRequest.of(page, size,sort));
    }

    public Object getAllBrandWithPagination(int page, int size, boolean isCrueltyFree, String country) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Brand> brandPage;
        if (StringUtils.isEmpty(country)) {
            // If country is empty, return all brands or filter by cruelty-free only
            if (isCrueltyFree) {
                brandPage = brandRepository.findByIsCrueltyFree(true, pageRequest);
            } else {
                brandPage = brandRepository.findAll(pageRequest);
            }
        } else {
            // If country is not empty, filter by country and cruelty-free status
            if (isCrueltyFree) {
                brandPage = brandRepository.findByIsCrueltyFreeAndCountry(true, country, pageRequest);
            } else {
                brandPage = brandRepository.findByCountry(country, pageRequest);
            }
        }
        PagnatedResponse<BrandResources> response = new PagnatedResponse<>();
        response.setContent(ToDTOList(brandPage.getContent()));
        response.setCurrentPage(brandPage.getNumber());
        response.setTotalPages(brandPage.getTotalPages());
        response.setTotalElements(brandPage.getTotalElements());
        response.setPageSize(brandPage.getSize());

        return response;
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Brand does not exist")
        );
    }

    public void addBrand(BrandDTO brandDTO) {
        brandRepository.findByName(brandDTO.getName()).ifPresent(
                (existingBrand) -> {
                    throw new RuntimeException("Brand already exists");
                }
        );
        brandRepository.save(new Brand(
                brandDTO.getId(),
                brandDTO.getName(),
                brandDTO.getDescription(),
                brandDTO.getCountry(),
                brandDTO.isCrueltyFree()
                ));
    }

    @Transactional
    public void updateBrand(BrandDTO brand, Long id) {
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

    private BrandResources ToDTO(Brand brand) {
        int totalProduct =productRepository.countByBrand(brand);

        float alcoholFreePercentage = ((float) productRepository.countByBrandAndAlcoholFreeIsTrue(brand)/totalProduct) * 100;
        float siliconFreePercentage = ((float) productRepository.countByBrandAndSiliconeFreeIsTrue(brand)/totalProduct) * 100;
        float sulfateFreePercentage = ((float) productRepository.countByBrandAndSulfateFreeIsTrue(brand) / totalProduct) * 100;
        float parabenFreePercentage = ((float) productRepository.countByBrandAndParabenFreeIsTrue(brand) / totalProduct) * 100;
        float oilFreePercentage = ((float) productRepository.countByBrandAndOilFreeIsTrue(brand) / totalProduct) * 100;
        float fungalAcneSafePercentage = ((float) productRepository.countByBrandAndFungalAcneSafeIsTrue(brand) / totalProduct) * 100;
        float euAllergenFreePercentage = ((float) productRepository.countByBrandAndEuAllergenFreeIsTrue(brand) / totalProduct) * 100;
        float reefSafePercentage = ((float) productRepository.countByBrandAndReefSafeIsTrue(brand) / totalProduct) * 100;
        float fragranceFreePercentage = ((float) productRepository.countByBrandAndFragranceFreeIsTrue(brand)/totalProduct) * 100;

        BrandResources resources = new BrandResources();
        resources.setId(brand.getId());
        resources.setName(brand.getName());
        resources.setDescription(brand.getDescription());
        resources.setCountry(brand.getCountry());
        resources.setCrueltyFree(brand.isCrueltyFree());
        resources.setTotalProduct(totalProduct);
        resources.setNoOfCleansers(productRepository.countByBrandAndSubcategory(brand,"Cleansers"));
        resources.setNoOfMasks(productRepository.countByBrandAndSubcategory(brand,"Masks"));
        resources.setNoOfMoisturizers(productRepository.countByBrandAndSubcategory(brand,"Moisturizers"));
        resources.setNoOfTreatment(productRepository.countByBrandAndSubcategory(brand,"Treatments"));
        resources.setAlcoholFreePercentage((int) alcoholFreePercentage);
        resources.setSiliconFreePercentage((int) siliconFreePercentage);
        resources.setSulfateFreePercentage((int) sulfateFreePercentage);
        resources.setParabenFreePercentage((int) parabenFreePercentage);
        resources.setOilFreePercentage((int) oilFreePercentage);
        resources.setFungalAcneSafePercentage((int) fungalAcneSafePercentage);
        resources.setEuAllergenFreePercentage((int) euAllergenFreePercentage);
        resources.setReefSafePercentage((int) reefSafePercentage);
        resources.setFragranceFreePercentage((int) fragranceFreePercentage);

        return resources;
    }

    private List<BrandResources> ToDTOList(List<Brand> brands) {
        return brands.stream().map(this::ToDTO).collect(Collectors.toList());
    }
}
