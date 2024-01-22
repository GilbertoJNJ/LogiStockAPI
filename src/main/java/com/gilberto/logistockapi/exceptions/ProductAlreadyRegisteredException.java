package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;

public final class ProductAlreadyRegisteredException extends HttpException {
    
    public ProductAlreadyRegisteredException() {
        super("Product already registered!", HttpStatus.BAD_REQUEST);
    }
}
