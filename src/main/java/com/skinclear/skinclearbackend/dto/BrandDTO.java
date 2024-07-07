package com.skinclear.skinclearbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandDTO {
    private Long id;
    private String name;
    private String description;
    private String country;
    private boolean isCrueltyFree;
}
