package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public sealed class ProductBaseForm permits ProductForm {
  
  @NotNull
  @Size(min = 2, max = 100)
  private String name;
  
  @NotNull
  private Category category;
  
  @Valid
  private SupplierForm supplier;
  
  @NotNull
  private BigDecimal unitPrice;
  
  @NotNull
  private MeasureUnit measureUnit;
  
  private String description;
  
}
