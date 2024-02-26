package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Ingredient;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{

    @Override
    @NonNull
    Page<Ingredient> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    List<Ingredient> findAll();

    @Override
    Optional<Ingredient> findById(@NonNull Long id);

    Optional<Ingredient> findByName(@NonNull String name);

    long count();
}
