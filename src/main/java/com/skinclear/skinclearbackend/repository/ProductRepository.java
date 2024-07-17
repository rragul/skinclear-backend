package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT p FROM Product p WHERE " +
            "(:subCategory IS NULL OR p.subcategory = :subCategory) AND " +
            "(:preference IS NULL OR " +
            "    (:preference = 'vegan' AND p.vegan = true) OR " +
            "    (:preference = 'alcohol_free' AND p.alcoholFree = true) OR " +
            "    (:preference = 'fragrance_free' AND p.fragranceFree = true) OR " +
            "    (:preference = 'silicone_free' AND p.siliconeFree = true) OR " +
            "    (:preference = 'sulfate_free' AND p.sulfateFree = true) OR " +
            "    (:preference = 'paraben_free' AND p.parabenFree = true) OR " +
            "    (:preference = 'oil_free' AND p.oilFree = true) OR " +
            "    (:preference = 'fungal_acne_safe' AND p.fungalAcneSafe = true) OR " +
            "    (:preference = 'eu_allergen_free' AND p.euAllergenFree = true) OR " +
            "    (:preference = 'reef_safe' AND p.reefSafe = true)) AND " +
            "(:benefits IS NULL OR p.whatItIs LIKE %:benefits%) AND " +
"(:type IS NULL OR p.whatItIs = :type) AND " +
            "(:ingredient IS NULL OR :ingredient IN (SELECT i.name FROM p.ingredients i)) AND " +
            "(:brand IS NULL OR p.brand.name = :brand)")
    Page<Product> findProductsByFilter(
            @Param("subCategory") String subCategory,
            @Param("preference") String preference,
            @Param("benefits") String benefits,
            @Param("type") String type,
            @Param("ingredient") String ingredient,
            @Param("brand") String brand,
            PageRequest pageRequest
    );

    @Query("SELECT COUNT(p) FROM Product p JOIN p.ingredients i WHERE i.id = :ingredientId")
    int countProductsByIngredientId(@Param("ingredientId") Long ingredientId);

    @Query(value = "SELECT * FROM product ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Product> findFirst10();

    List<Product> findTop10ByNameStartingWithIgnoreCase(String keyword);
}
