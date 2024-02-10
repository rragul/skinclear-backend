package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientInsightRepository extends JpaRepository<IngredientInsight, Long>{
}
