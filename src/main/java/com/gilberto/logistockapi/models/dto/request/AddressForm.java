package com.gilberto.logistockapi.models.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressForm(
    @NotNull
    @Size(min = 2, max = 100)
    String street,
    
    @NotNull
    String number,
    
    @NotNull
    String district,
    
    @NotNull
    String cityName,
    
    @NotNull
    String stateName,
    
    @NotNull
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Postal code must be XXXXX-XXX!")
    String postalCode,
    
    String complement
) {
  
}
