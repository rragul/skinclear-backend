package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Ingredient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
    Optional<Ingredient> findByName(@NonNull String name);
    List<Ingredient> findTop10ByNameStartingWithIgnoreCase(String keyword);
    @Query(value = "SELECT * FROM ingredient ORDER BY RANDOM() LIMIT 10", nativeQuery = true)
    List<Ingredient> findFirst10();

    Optional<Ingredient> findByNameIgnoreCase(String name);
}
