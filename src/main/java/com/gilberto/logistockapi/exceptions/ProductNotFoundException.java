package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;

public final class ProductNotFoundException extends HttpException {

    public ProductNotFoundException() {
        super("Product not found!", HttpStatus.NOT_FOUND);
    }
}
