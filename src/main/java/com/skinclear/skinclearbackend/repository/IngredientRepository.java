package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
}
