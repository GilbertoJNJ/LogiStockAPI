package com.gilberto.logistockapi.utils;

import com.gilberto.logistockapi.models.dto.request.ProductFilter;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.ProductUpdateForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.models.enums.Category;
import com.gilberto.logistockapi.models.enums.MeasureUnit;
import java.math.BigDecimal;
import java.util.Arrays;

public class ModelUtils {
  
  public static ProductForm getProductForm() {
    return new ProductForm(
        "Product's name",
        "barcode",
        Category.CLOTHING,
        null,
        BigDecimal.valueOf(1.99),
        10,
        100,
        MeasureUnit.UNIT,
        "description"
    );
  }
  
  public static ProductFilter getProductFilter() {
    return new ProductFilter(
        1,
        10,
        "search",
        Arrays.asList(Category.values())
    );
  }
  
  public static Product getProduct() {
    return Product.builder()
        .id(1L)
        .name("Product's name")
        .category(Category.FOOD)
        .description("description")
        .unitPrice(BigDecimal.valueOf(1.99))
        .barCode("barcode")
        .stockQuantity(10)
        .measureUnit(MeasureUnit.KILOGRAM)
        .maxStockLevel(100)
        .build();
  }
  
  public static ProductUpdateForm getProductUpdateForm() {
    return new ProductUpdateForm(
        "Product's name",
        Category.CLOTHING,
        null,
        BigDecimal.valueOf(1.99),
        100,
        MeasureUnit.UNIT,
        "description"
    );
  }
  
  public static ProductDTO getProductDTO() {
    return new ProductDTO(
        1L,
        "Product's name",
        "barcode",
        Category.CLOTHING,
        null,
        BigDecimal.valueOf(1.99),
        MeasureUnit.PACK,
        10,
        "description");
  }
  
}
