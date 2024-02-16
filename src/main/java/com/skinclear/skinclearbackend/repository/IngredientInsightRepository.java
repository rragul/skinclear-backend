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

@Repository
public interface IngredientInsightRepository extends JpaRepository<IngredientInsight, Long> {

    @Override
    @NonNull
    Page<IngredientInsight> findAll(@NonNull Pageable pageable);

    @Query(value = "SELECT * FROM ingredient_insight ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<IngredientInsight> findRandomImages(@Param("limit") int limit);

    @Query(value = "SELECT * FROM ingredient_insight WHERE id IN (:ids)", nativeQuery = true)
    List<IngredientInsight> findIngredientInsightByIds(@Param("ids") List<Long> ids);

    @Query(value = "SELECT image FROM ingredient_insight WHERE id IN (:ids)", nativeQuery = true)
    List<String> findIngredientInsightImageByIds(@Param("ids") List<Long> ids);


}
