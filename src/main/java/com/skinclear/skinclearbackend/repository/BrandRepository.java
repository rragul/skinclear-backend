package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>{
    Optional<Object> findByName(String name);
}
