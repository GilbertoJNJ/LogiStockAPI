package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.request.ProductBaseForm;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.request.QuantityForm;
import com.gilberto.logistockapi.models.dto.response.ProductDTO;
import java.util.List;
import javax.validation.Valid;

public interface IProductService {
  
  ProductDTO create(ProductForm productDTO) throws ProductAlreadyRegisteredException;
  
  List<ProductDTO> listAll();
  
  ProductDTO findByBarCode(String name) throws ProductNotFoundException;
  
  void delete(Long id) throws ProductNotFoundException;
  
  ProductDTO updateById(Long id, ProductBaseForm productDTO) throws ProductNotFoundException;
  
  ProductDTO increment(Long id, @Valid QuantityForm quantity) throws ProductNotFoundException, ProductStockExceededException;
  
  ProductDTO decrement(Long id, @Valid QuantityForm quantity) throws ProductNotFoundException, ProductStockUnderThanZeroException;
  
}
