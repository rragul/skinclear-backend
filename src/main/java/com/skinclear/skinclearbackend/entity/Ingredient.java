package com.skinclear.skinclearbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String typeId;
    private String whatItDoes;
    private String benefitsId;
    private String otherNames;
    private String concernId;
    private String rarity;
    private Integer likeCount;
    private Integer dislikeCount;
    @Column(columnDefinition = "TEXT")
    private String explain;
}
