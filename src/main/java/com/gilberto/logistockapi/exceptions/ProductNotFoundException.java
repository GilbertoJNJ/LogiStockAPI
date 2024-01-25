package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ProductNotFoundException extends HttpException {

    public ProductNotFoundException() {
        super("Product not found!", HttpStatus.NOT_FOUND);
    }
}
