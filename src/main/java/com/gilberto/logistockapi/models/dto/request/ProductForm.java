package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {
  
  @NotNull
  @Size(min = 2, max = 100)
  private String name;
  
  @NotNull
  @Size(min = 2, max = 100)
  private String barCode;
  
  @NotNull
  private Category category;
  
  @Valid
  private SupplierForm supplier;
  
  @NotNull
  private BigDecimal unitPrice;
  
  @NotNull
  private MeasureUnit measureUnit;
  
  private Integer stockQuantity;
  
  private Integer minStockLevel;
  
  private Integer maxStockLevel;
  
  private String description;
  
  public ProductForm(Product product) {
    this.name          = product.getName();
    this.unitPrice     = product.getUnitPrice();
    this.maxStockLevel = product.getMaxStockLevel();
  }
  
}
