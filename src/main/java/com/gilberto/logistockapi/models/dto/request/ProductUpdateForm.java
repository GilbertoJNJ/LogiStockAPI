package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public record ProductUpdateForm(
    @NotNull
    @Size(min = 2, max = 100)
    String name,
    
    @NotNull
    Category category,
    
    @Valid
    SupplierForm supplier,
    
    @NotNull
    BigDecimal unitPrice,
    
    @NotNull
    Integer maxStockLevel,
    
    @NotNull
    MeasureUnit measureUnit,
    
    String description
) {

}
