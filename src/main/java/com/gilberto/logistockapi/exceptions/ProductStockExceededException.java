package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;

public final class ProductStockExceededException extends HttpException{
    
    public ProductStockExceededException() {
        super("Quantity exceeds maximum stock capacity!", HttpStatus.BAD_REQUEST);
    }
}
