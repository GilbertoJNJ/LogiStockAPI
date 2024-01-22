package com.gilberto.logistockapi.models.dto.request;

import jakarta.validation.constraints.NotNull;

public record QuantityForm(
    @NotNull
    Integer quantity
) {

}
