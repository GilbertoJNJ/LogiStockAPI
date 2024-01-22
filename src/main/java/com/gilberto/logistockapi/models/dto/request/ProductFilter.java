package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.enums.Category;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProductFilter(
    @NotNull
    Integer pageNumber,
    
    @NotNull
    Integer pageSize,
    
    String search,
    
    List<Category> categories
) {

}
