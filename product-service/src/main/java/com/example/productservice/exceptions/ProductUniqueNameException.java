package com.example.productservice.exceptions;

import com.example.productservice.model.dto.ProductRequest;
import com.example.productservice.model.dto.ProductResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductUniqueNameException extends RuntimeException {
    private ProductRequest productRequest;
    public ProductUniqueNameException(String message, ProductRequest productRequest) {
        super(message);
        this.productRequest = productRequest;
    }
}
