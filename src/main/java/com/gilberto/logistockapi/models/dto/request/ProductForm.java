package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;


public record ProductForm(
    @NotNull
    @Size(min = 2, max = 100)
    String name,
    
    @NotNull
    @Size(min = 2, max = 100)
    String barCode,
    
    @NotNull
    Category category,
    
    @Valid
    SupplierForm supplier,
    
    @NotNull
    BigDecimal unitPrice,
    
    Integer stockQuantity,
    
    @NotNull
    Integer maxStockLevel,
    
    @NotNull
    MeasureUnit measureUnit,
    
    String description
) {

}
