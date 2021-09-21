package com.gilberto.productsapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductStockUnderThanZeroException extends Exception{
    public ProductStockUnderThanZeroException(Long id, int quantityToDecrement) {
        super(String.format("Product with %s ID to decrement informed results in a stock's quantity less than zero: %s", id, quantityToDecrement));
    }
}
