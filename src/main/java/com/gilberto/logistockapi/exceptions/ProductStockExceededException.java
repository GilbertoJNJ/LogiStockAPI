package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class ProductStockExceededException extends HttpException{
    
    public ProductStockExceededException() {
        super("Quantity exceeds maximum stock capacity!", HttpStatus.BAD_REQUEST);
    }
}
