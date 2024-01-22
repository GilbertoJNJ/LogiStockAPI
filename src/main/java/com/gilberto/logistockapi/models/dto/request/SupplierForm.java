package com.gilberto.logistockapi.models.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SupplierForm(
    @NotNull
    @Size(min = 2, max = 100)
    String name,
    
    @NotNull
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
             message = "Document must be a CPF or CNPJ!")
    String legalDocument,
    
    @Email
    String email,
    
    @Pattern(regexp = "\\(?(\\d{2,3})\\)?[-.\\s]?\\d{4,5}[-.\\s]?\\d{4}",
             message = "Must be a phone number formatted!")
    String phone,
    
    @Valid
    AddressForm address
) {
  
}
