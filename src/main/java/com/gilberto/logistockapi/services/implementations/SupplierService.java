package com.gilberto.logistockapi.services.implementations;

import com.gilberto.logistockapi.models.dto.request.AddressForm;
import com.gilberto.logistockapi.models.dto.request.SupplierForm;
import com.gilberto.logistockapi.models.entity.Address;
import com.gilberto.logistockapi.models.entity.Supplier;
import com.gilberto.logistockapi.repositories.ISupplierRepository;
import com.gilberto.logistockapi.services.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierService implements ISupplierService {
  
  private final ISupplierRepository supplierRepository;
  
  public SupplierService(@Autowired ISupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }
  
  @Override
  public Supplier save(SupplierForm supplierForm) {
    if (supplierForm == null) {
      return null;
    }
    
    return this.supplierRepository.findByLegalDocument(supplierForm.legalDocument())
        .orElseGet(() -> {
          var supplier = Supplier.builder()
              .name(supplierForm.name())
              .legalDocument(supplierForm.legalDocument())
              .email(supplierForm.email())
              .phone(supplierForm.phone())
              .address(createAddress(supplierForm.address()))
              .build();
          return this.supplierRepository.save(supplier);
        });
    
  }
  
  private Address createAddress(AddressForm addressForm) {
    if (addressForm == null) {
      return null;
    }
    
    return Address.builder()
        .street(addressForm.street())
        .number(addressForm.number())
        .district(addressForm.district())
        .cityName(addressForm.cityName())
        .stateName(addressForm.stateName())
        .postalCode(addressForm.postalCode())
        .complement(addressForm.complement())
        .build();
  }
  
}
