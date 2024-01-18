package com.gilberto.logistockapi.models.entity;

import com.gilberto.logistockapi.models.dto.request.ProductDTO;
import com.gilberto.logistockapi.models.enums.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    public Product( ProductDTO dto) {
        this.name = dto.getName();
        this.buyPrice = dto.getBuyPrice();
        this.sellPrice = dto.getSellPrice();
        this.quantity = dto.getQuantity();
        this.maxQuantity = dto.getMaxQuantity();
    }
}
