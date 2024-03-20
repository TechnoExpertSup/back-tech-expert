package com.example.productservice.model.dto;

import java.math.BigDecimal;

public record ProductResponse(Long productId,
                              String productName,
                              BigDecimal productPrice,
                              String productDescription,
                              int productQuantity) {
}
