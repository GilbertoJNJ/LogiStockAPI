package com.gilberto.logistockapi.services;

import com.gilberto.logistockapi.models.dto.request.SupplierForm;
import com.gilberto.logistockapi.models.entity.Supplier;

public interface ISupplierService {
  
  Supplier save(SupplierForm supplierForm);
  
}
