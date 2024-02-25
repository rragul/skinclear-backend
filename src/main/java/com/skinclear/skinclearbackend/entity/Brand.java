package com.skinclear.skinclearbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String country;
    private boolean isCrueltyFree;

    public void updateFrom(Brand brand) {
        if (brand.getName() != null) {
            this.setName(brand.getName());
        }
        if (brand.getDescription() != null) {
            this.setDescription(brand.getDescription());
        }
        if (brand.getCountry() != null) {
            this.setCountry(brand.getCountry());
        }
        this.setCrueltyFree(brand.isCrueltyFree());
    }
}
