package com.gilberto.logistockapi.mappers;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.models.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper implements IProductMapper{
  
  @Override
  public Product toProduct(ProductForm productForm) {
    var product = new Product();
    product.setName(productForm.getName());
    product.setCategory(productForm.getCategory());
    product.setDescription(productForm.getDescription());
    product.setUnitPrice(productForm.getUnitPrice());
    product.setBarCode(productForm.getBarCode());
    product.setStockQuantity(productForm.getStockQuantity());
    product.setMeasureUnit(productForm.getMeasureUnit());
    product.setMinStockLevel(productForm.getMinStockLevel());
    product.setMaxStockLevel(productForm.getMaxStockLevel());
    product.setSupplier(null);
    return product;
  }
  
  @Override
  public ProductDTO toProductDTO(Product product) {
    return new ProductDTO(product.getId(), product.getName(), product.getBarCode(),
        product.getCategory(), null, product.getUnitPrice(), product.getMeasureUnit(),
        product.getStockQuantity(), product.getDescription());
  }
  
}
