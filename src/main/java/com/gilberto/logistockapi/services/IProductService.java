package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.request.ProductFilter;
import com.gilberto.logistockapi.models.dto.request.ProductUpdateForm;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import java.util.List;

public interface IProductService {
  
  ProductDTO create(ProductForm productForm) throws ProductAlreadyRegisteredException;
  
  List<ProductDTO> listAll(ProductFilter filter);
  
  ProductDTO findById(Long id) throws ProductNotFoundException;
  
  ProductDTO findByBarCode(String barCode) throws ProductNotFoundException;
  
  void delete(Long id) throws ProductNotFoundException;
  
  ProductDTO updateById(Long id, ProductUpdateForm updateForm) throws ProductNotFoundException;
  
  ProductDTO increment(Long id, QuantityForm quantity)
      throws ProductNotFoundException, ProductStockExceededException;
  
  ProductDTO decrement(Long id, QuantityForm quantity)
      throws ProductNotFoundException, ProductStockUnderThanZeroException;
  
}
