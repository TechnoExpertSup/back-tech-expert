package com.example.productservice.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
public record ProductRequest(@NotNull(message = "Please enter name of product")
                             @Size(min = 3, message = "Product`s name should contain 3 or more symbols")
                             String productName,
                             @Min(value = 0, message = "Product`s price should be more then 0")
                             BigDecimal productPrice,
                             String productDescription,
                             @Min(value = 0,message = "Quantity can`t be less then 0")
                             int productQuantity) {

}
