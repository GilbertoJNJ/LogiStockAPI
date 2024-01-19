package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.exceptions.ProductAlreadyRegisteredException;
import com.gilberto.logistockapi.exceptions.ProductNotFoundException;
import com.gilberto.logistockapi.exceptions.ProductStockExceededException;
import com.gilberto.logistockapi.exceptions.ProductStockUnderThanZeroException;
import com.gilberto.logistockapi.models.dto.request.ProductForm;
import com.gilberto.logistockapi.models.dto.response.MessageResponseDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {
  
  MessageResponseDTO createProduct(ProductForm productDTO) throws ProductAlreadyRegisteredException;
  
  List<ProductForm> listAll();
  
  ProductForm findByBarCode(String name) throws ProductNotFoundException;
  
  MessageResponseDTO delete(Long id) throws ProductNotFoundException;
  
  MessageResponseDTO updateById(Long id, ProductForm productDTO) throws ProductNotFoundException;
  
  ProductForm increment(Long id, Integer quantityToIncrement) throws ProductNotFoundException, ProductStockExceededException;
  
  ProductForm decrement(Long id, Integer quantityToDecrement) throws ProductNotFoundException, ProductStockUnderThanZeroException;
  
}
