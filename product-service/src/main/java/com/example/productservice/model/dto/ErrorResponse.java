package com.example.productservice.model.dto;

public record ErrorResponse<T>(String message, T request) {
}
