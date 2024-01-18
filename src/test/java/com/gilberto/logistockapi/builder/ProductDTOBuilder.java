package com.gilberto.logistockapi.builder;

import com.gilberto.logistockapi.models.dto.request.CategoryDTO;
import com.gilberto.logistockapi.models.dto.request.ProductDTO;
import lombok.Builder;


@Builder
public class ProductDTOBuilder {


    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "cerveja";

    @Builder.Default
    private String buyPrice = "33.5";

    @Builder.Default
    private String sellPrice = "40.0";

    @Builder.Default
    private CategoryDTO categoryDTO = CategoryDTO.builder().id(1L).category("bebida").build();

    @Builder.Default
    private int quantity = 10;

    @Builder.Default
    private int maxQuantity = 100;


    public ProductDTO toProductDTO() {
        return new ProductDTO(id, name, buyPrice, sellPrice, categoryDTO, quantity, maxQuantity);
    }
}
