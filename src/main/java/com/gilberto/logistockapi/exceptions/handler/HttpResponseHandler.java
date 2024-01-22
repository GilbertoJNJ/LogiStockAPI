package com.gilberto.logistockapi.exceptions.handler;

import com.gilberto.logistockapi.exceptions.HttpException;
import com.gilberto.logistockapi.models.dto.response.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class HttpResponseHandler extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler(HttpException.class)
  public ResponseEntity<ErrorDTO> handlerHttpException(HttpException exception) {
    return ResponseEntity.status(exception.getStatus())
        .body(new ErrorDTO(exception.getStatus().value(), exception.getMessage()));
  }

}
