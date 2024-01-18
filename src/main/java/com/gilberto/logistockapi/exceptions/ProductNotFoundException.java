package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(Long id) {
        super("Product not found with ID " + id);
    }
    public ProductNotFoundException(String name) { super(String.format("Product not found with Name " + name));
    }
}
