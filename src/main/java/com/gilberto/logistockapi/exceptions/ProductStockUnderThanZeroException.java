package com.gilberto.logistockapi.exceptions;

import org.springframework.http.HttpStatus;

public final class ProductStockUnderThanZeroException extends HttpException{
    
    public ProductStockUnderThanZeroException() {
        super("Insufficient quantity in stock!", HttpStatus.BAD_REQUEST);
    }
}
