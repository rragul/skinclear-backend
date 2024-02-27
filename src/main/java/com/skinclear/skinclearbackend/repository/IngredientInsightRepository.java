package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.IngredientInsight;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientInsightRepository extends JpaRepository<IngredientInsight, Long> {

    @Query(value = "SELECT * FROM ingredient_insight ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<IngredientInsight> findRandomImages(@Param("limit") int limit);

    Optional<IngredientInsight> findByName(@NonNull String name);

    List<IngredientInsight> findByTypeIgnoreCase(String type);
}
