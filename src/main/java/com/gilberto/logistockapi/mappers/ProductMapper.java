package com.gilberto.logistockapi.mappers;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.models.dto.response.SupplierDTO;
import com.gilberto.logistockapi.models.entity.Product;
import com.gilberto.logistockapi.models.entity.Supplier;

public class ProductMapper implements IProductMapper {
  
  @Override
  public Product toProduct(ProductForm productForm) {
    return Product.builder()
        .name(productForm.name())
        .category(productForm.category())
        .description(productForm.description())
        .unitPrice(productForm.unitPrice())
        .barCode(productForm.barCode())
        .stockQuantity(productForm.stockQuantity())
        .measureUnit(productForm.measureUnit())
        .maxStockLevel(productForm.maxStockLevel())
        .build();
  }
  
  @Override
  public ProductDTO toProductDTO(Product product) {
    return new ProductDTO(
        product.getId(),
        product.getName(),
        product.getBarCode(),
        product.getCategory(),
        createSupplierDTO(product.getSupplier()),
        product.getUnitPrice(),
        product.getMeasureUnit(),
        product.getStockQuantity(),
        product.getDescription()
    );
  }
  
  private SupplierDTO createSupplierDTO(Supplier supplier) {
    if (supplier == null) {
      return null;
    }
    
    return new SupplierDTO(
        supplier.getId(),
        supplier.getName(),
        supplier.getLegalDocument(),
        supplier.getEmail(),
        supplier.getPhone()
    );
  }
  
}
