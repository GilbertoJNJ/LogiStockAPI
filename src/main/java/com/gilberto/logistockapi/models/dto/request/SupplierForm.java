package com.gilberto.logistockapi.models.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierForm {
  
  @NotNull
  @Size(min = 2, max = 100)
  private String name;
  
  @NotNull
  private String legalDocument;
  
  private String email;
  
  private String phone;
  
  @Valid
  private AddressForm address;
  
}
