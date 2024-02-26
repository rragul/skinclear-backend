package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>{
    Optional<Object> findByName(String name);

    List<Brand> findTop20ByNameContainingIgnoreCase(String name);
}
