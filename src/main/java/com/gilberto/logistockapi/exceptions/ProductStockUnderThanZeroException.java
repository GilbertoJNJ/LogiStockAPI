package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class ProductStockUnderThanZeroException extends HttpException{
    
    public ProductStockUnderThanZeroException() {
        super("Insufficient quantity in stock!", HttpStatus.BAD_REQUEST);
    }
}
