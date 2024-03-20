package com.example.productservice.exceptions;

import lombok.Getter;

@Getter
public  class  ProductNotFoundException extends RuntimeException {
    private  Object request;
    public  ProductNotFoundException(String message, Object request) {
        super(message);
        this.request = request;
    }
}
