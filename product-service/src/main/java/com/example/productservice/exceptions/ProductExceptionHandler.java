package com.example.productservice.exceptions;

import com.example.productservice.model.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }
    @ExceptionHandler(ProductUniqueNameException.class)
    protected ResponseEntity<?> handleDuplicateKeyException(ProductUniqueNameException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(),ex.getProductRequest()));
    }
    @ExceptionHandler(ProductNotFoundException.class)
    protected ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(),ex.getProductRequest()));
    }
}
