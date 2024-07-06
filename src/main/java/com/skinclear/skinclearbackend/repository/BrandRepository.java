package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>{
    Optional<Brand> findByName(String updatedName);
    List<Brand> findTop10ByNameStartingWithIgnoreCase(String searchKeyword);

}
