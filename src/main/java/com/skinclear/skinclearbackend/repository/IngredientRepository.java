package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Ingredient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>{

    @Override
    @NonNull
    Page<Ingredient> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    List<Ingredient> findAll();

    long count();
}
