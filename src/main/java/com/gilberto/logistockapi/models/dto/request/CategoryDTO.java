package com.gilberto.logistockapi.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {


    private Long id;

    @Size(min = 2, max = 100)
    private String category;
}