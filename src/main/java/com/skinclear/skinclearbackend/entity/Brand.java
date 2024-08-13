package com.skinclear.skinclearbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(length = 1000)
    private String description;
    private String country;
    private boolean isCrueltyFree;
    @JsonBackReference
    @OneToMany(mappedBy = "brand")
    private List<Product> products;

    public  Brand(Long id ,String name , String description , String country , Boolean isCrueltyFree){
        this.id=id;
        this.name=name;
        this.description=description;
        this.country = country;
        this.isCrueltyFree=isCrueltyFree;
    }
}
