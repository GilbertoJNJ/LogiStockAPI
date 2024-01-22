package com.gilberto.logistockapi.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public sealed class HttpException extends Exception permits ProductAlreadyRegisteredException,
    ProductNotFoundException, ProductStockExceededException, ProductStockUnderThanZeroException {
  
  private HttpStatus status;
  
  public HttpException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
  
}
