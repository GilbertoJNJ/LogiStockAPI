package com.gilberto.logistockapi.mappers;

import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import com.gilberto.logistockapi.models.entity.Product;

public interface IProductMapper {
  
  Product toProduct(ProductForm productForm);
  
  ProductDTO toProductDTO(Product product);

}
