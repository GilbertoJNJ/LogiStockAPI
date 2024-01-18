package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private String buyPrice;

    @NotNull
    private String sellPrice;

    @Valid
    private CategoryDTO category;

    @NotNull
    @Max(100)
    private Integer quantity;

    @NotNull
    @Max(500)
    private Integer maxQuantity;
    
    public ProductDTO( Product product) {
        this.name = product.getName();
        this.buyPrice = product.getBuyPrice();
        this.sellPrice = product.getSellPrice();
        this.quantity = product.getQuantity();
        this.maxQuantity = product.getMaxQuantity();
    }

}
