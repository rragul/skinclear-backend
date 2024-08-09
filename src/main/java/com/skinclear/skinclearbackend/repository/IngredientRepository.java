package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Ingredient;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
    Optional<Ingredient> findByName(@NonNull String name);
    List<Ingredient> findTop10ByNameStartingWithIgnoreCase(String keyword);
    @Query(value = "SELECT i FROM Ingredient i ORDER BY RANDOM() LIMIT 10")
    List<Ingredient> findFirst10();

    Optional<Ingredient> findByNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM Ingredient i WHERE LOWER(i.name) IN :names OR EXISTS (SELECT 1 FROM unnest(string_to_array(LOWER(i.other_names), ',')) AS name WHERE name IN :names)", nativeQuery = true)
    List<Ingredient> findByNameOrOtherNames(@Param("names") List<String> names);


}
