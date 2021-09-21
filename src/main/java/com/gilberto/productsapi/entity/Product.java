package com.gilberto.productsapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private String name;

    @Column(nullable = false)
    private String buyPrice;

    @Column(nullable = false)
    private String sellPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Category category;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Integer maxQuantity;
}
