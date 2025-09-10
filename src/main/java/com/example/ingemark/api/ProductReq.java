package com.example.ingemark.api;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductReq {

    @NotNull(message = "Product code is required")
    @Size(min = 10, max = 10, message = "Product code must be exactly 10 characters long")
    private String code;

    @NotNull(message = "Product name is required")
    @NotEmpty(message = "Product name cannot be empty")
    private String name;

    @NotNull(message = "Product price is required")
    @PositiveOrZero(message = "Product price must be non-negative")
    private Float price_eur;

    @NotNull(message = "Product availability must be specified")
    private Boolean is_available;

}
