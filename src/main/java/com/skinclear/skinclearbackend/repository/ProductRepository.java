package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findProductByName(String name);

    int countByBrand(Brand brand);
    int countByBrandAndSubcategory(Brand brand, String subcategory);
    int countByBrandAndAlcoholFreeIsTrue(Brand brand);
    int countByBrandAndFragranceFreeIsTrue(Brand brand);
    int countByBrandAndSiliconeFreeIsTrue(Brand brand);
    int countByBrandAndSulfateFreeIsTrue(Brand brand);
    int countByBrandAndParabenFreeIsTrue(Brand brand);
    int countByBrandAndOilFreeIsTrue(Brand brand);
    int countByBrandAndFungalAcneSafeIsTrue(Brand brand);
    int countByBrandAndEuAllergenFreeIsTrue(Brand brand);
    int countByBrandAndReefSafeIsTrue(Brand brand);
    List<Product> findProductByIngredients(Ingredient ingredients);

    Object findProductBySubcategory(String subCategory, PageRequest of);
}
