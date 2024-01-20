package com.gilberto.logistockapi.models.dto.request;

import com.gilberto.logistockapi.models.entity.Product;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class ProductForm extends ProductBaseForm {
  
  @NotNull
  @Size(min = 2, max = 100)
  private String barCode;
  
  private Integer stockQuantity = 0;
  
  private Integer minStockLevel = 10;
  
  private Integer maxStockLevel = 100;
  
  public ProductForm(Product product) {
    this.setName(product.getName());
    this.setUnitPrice(product.getUnitPrice());
    this.maxStockLevel = product.getMaxStockLevel();
  }
  
}
