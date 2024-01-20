package com.gilberto.logistockapi.models.dto.response;

import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import java.math.BigDecimal;

public record ProductDTO(
    Long id,
    String name,
    String barCode,
    Category category,
    SupplierDTO supplier,
    BigDecimal unitPrice,
    MeasureUnit measureUnit,
    Integer stockQuantity,
    String description
) {

}
