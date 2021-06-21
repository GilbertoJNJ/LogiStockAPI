package com.gilberto.storeapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {


    private Long id;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String Name;

    @NotEmpty
    private String buyPrice;

    @NotEmpty
    private String sellPrice;

    @Valid
    @NotEmpty
    private List<CategoryDTO> category;
}
