package com.skinclear.skinclearbackend.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandResources {
    private Long id;
    private String name;
    private String description;
    private String country;
    private boolean isCrueltyFree;
    private  int totalProduct;
    private  int noOfCleansers;
    private  int noOfMasks;
    private  int noOfTreatment;
    private int noOfMoisturizers;
    private  int alcoholFreePercentage;
    private  int siliconFreePercentage;
    private  int fragranceFreePercentage;
    private  int sulfateFreePercentage;
    private  int parabenFreePercentage;
    private  int oilFreePercentage;
    private  int fungalAcneSafePercentage;
    private  int euAllergenFreePercentage;
    private  int veganPercentage;
}
