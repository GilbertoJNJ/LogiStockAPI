package com.gilberto.logistockapi.models.dto.request;

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
public class AddressForm {
  
  @NotNull
  @Size(min = 2, max = 100)
  private String street;
  
  @NotNull
  private String number;
  
  @NotNull
  private String district;
  
  @NotNull
  private String cityName;
  
  @NotNull
  private String stateName;
  
  @NotNull
  private String postalCode;
  
  private String complement;
  
}
