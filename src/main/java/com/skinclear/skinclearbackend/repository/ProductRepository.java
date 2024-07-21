package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import com.skinclear.skinclearbackend.entity.Ingredient;
import com.skinclear.skinclearbackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.ingredients i LEFT JOIN i.benefits b WHERE " +
            "(:subCategory IS NULL OR :subCategory = '' OR p.subcategory = :subCategory) AND " +
            "(:#{#preferenceList.isEmpty()} = true OR " +
            "    (COALESCE(:#{#preferenceList.contains('vegan')}, false) = false OR p.vegan = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('alcohol_free')}, false) = false OR p.alcoholFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('fragrance_free')}, false) = false OR p.fragranceFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('silicone_free')}, false) = false OR p.siliconeFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('sulfate_free')}, false) = false OR p.sulfateFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('paraben_free')}, false) = false OR p.parabenFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('oil_free')}, false) = false OR p.oilFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('fungal_acne_safe')}, false) = false OR p.fungalAcneSafe = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('eu_allergen_free')}, false) = false OR p.euAllergenFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('reef_safe')}, false) = false OR p.reefSafe = true) " +
            ") AND " +
            "(:benefits IS NULL OR :benefits = '' OR " +
            "    EXISTS (SELECT 1 FROM p.ingredients ing JOIN ing.benefits b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :benefits, '%'))) " +
            ") AND " +
            "(:type IS NULL OR :type = '' OR p.type = :type) AND " +
            "(:#{#ingredientList.isEmpty()} = true OR " +
            "    (SELECT COUNT(ing) FROM p.ingredients ing WHERE ing.name IN :ingredientList) >= :#{#ingredientList.size()}) AND " +
            "(:brand IS NULL OR :brand = '' OR p.brand.name = :brand)")
    Page<Product> findProductsByFilter(
            @Param("subCategory") String subCategory,
            @Param("preferenceList") List<String> preferenceList,
            @Param("benefits") String benefits,
            @Param("type") String type,
            @Param("ingredientList") List<String> ingredientList,
            @Param("brand") String brand,
            PageRequest pageRequest
    );

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN p.ingredients i LEFT JOIN i.benefits b WHERE " +
            "(:subCategory IS NULL OR :subCategory = '' OR p.subcategory = :subCategory) AND " +
            "(:#{#preferenceList.isEmpty()} = true OR " +
            "    (COALESCE(:#{#preferenceList.contains('vegan')}, false) = false OR p.vegan = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('alcohol_free')}, false) = false OR p.alcoholFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('fragrance_free')}, false) = false OR p.fragranceFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('silicone_free')}, false) = false OR p.siliconeFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('sulfate_free')}, false) = false OR p.sulfateFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('paraben_free')}, false) = false OR p.parabenFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('oil_free')}, false) = false OR p.oilFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('fungal_acne_safe')}, false) = false OR p.fungalAcneSafe = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('eu_allergen_free')}, false) = false OR p.euAllergenFree = true) AND " +
            "    (COALESCE(:#{#preferenceList.contains('reef_safe')}, false) = false OR p.reefSafe = true) " +
            ") AND " +
            "(:benefits IS NULL OR :benefits = '' OR EXISTS (SELECT 1 FROM p.ingredients ing JOIN ing.benefits b WHERE LOWER(b.name) IN :benefits)) AND " +
            "(:type IS NULL OR :type = '' OR p.type = :type) AND " +
            "(:#{#ingredientList.isEmpty()} = true OR " +
            "    (SELECT COUNT(ing) FROM p.ingredients ing WHERE ing.name IN :ingredientList) >= :#{#ingredientList.size()}) AND " +
            "(:brand IS NULL OR :brand = '' OR p.brand.name = :brand)")
    Page<Product> findProductsByFilter(
            @Param("subCategory") String subCategory,
            @Param("preferenceList") List<String> preferenceList,
            @Param("benefits") List<String> benefits,
            @Param("type") String type,
            @Param("ingredientList") List<String> ingredientList,
            @Param("brand") String brand,
            PageRequest pageRequest
    );

    @Query("SELECT COUNT(p) FROM Product p JOIN p.ingredients i WHERE i.id = :ingredientId")
    int countProductsByIngredientId(@Param("ingredientId") Long ingredientId);

    @Query(value = "SELECT * FROM product ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Product> findFirst10();

    List<Product> findTop10ByNameStartingWithIgnoreCase(String keyword);
    @Query("SELECT p FROM Product p JOIN p.ingredients i WHERE p.type = :type AND i IN :ingredients GROUP BY p HAVING COUNT(i) >= 3")
    Page<Product> findProductByTypeAndIngredients(@Param("type") String type, @Param("ingredients") List<Ingredient> ingredients, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.id != :productId AND p.type = (SELECT p2.type FROM Product p2 WHERE p2.id = :productId)")
    List<Product> findSimilarProducts(Long productId, Pageable pageable);
}
