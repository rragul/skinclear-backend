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
    Optional<Object> findByName(@NonNull String name);

    // Query to find ingredients by name containing the keyword in a case-insensitive manner and from beginning
    @Query("SELECT i FROM Ingredient i WHERE lower(i.name) LIKE lower(concat(?1, '%')) LIMIT 10")
    List<Ingredient> findIngredientsByName(String keyword);
}
