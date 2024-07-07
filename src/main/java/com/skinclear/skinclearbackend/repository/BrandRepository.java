package com.skinclear.skinclearbackend.repository;

import com.skinclear.skinclearbackend.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>{
    Optional<Brand> findByName(String updatedName);
    List<Brand> findTop10ByNameStartingWithIgnoreCase(String searchKeyword);

    Page<Brand> findByIsCrueltyFreeAndCountry(boolean isCrueltyFree, String country, Pageable pageable);

    Page<Brand> findByCountry(String country, Pageable pageable);

    // Method to find all brands filtered by isCrueltyFree only
    Page<Brand> findByIsCrueltyFree(boolean isCrueltyFree, Pageable pageable);

    // Method to find all brands without filtering by isCrueltyFree and country
    Page<Brand> findAll(Pageable pageable);

}
