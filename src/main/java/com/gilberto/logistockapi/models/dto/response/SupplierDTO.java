package com.gilberto.logistockapi.models.dto.response;

public record SupplierDTO(
    Long id,
    String name,
    String legalDocument,
    String email,
    String phone
) {

}
