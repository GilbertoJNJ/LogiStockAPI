package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class ProductAlreadyRegisteredException extends HttpException {
    
    public ProductAlreadyRegisteredException() {
        super("Product already registered!", HttpStatus.BAD_REQUEST);
    }
}
