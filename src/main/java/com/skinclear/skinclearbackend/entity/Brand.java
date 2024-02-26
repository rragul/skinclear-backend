package com.skinclear.skinclearbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
