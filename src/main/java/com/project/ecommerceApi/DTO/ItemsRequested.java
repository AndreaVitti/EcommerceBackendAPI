package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemsRequested {
    @NotNull(message = "Product id required")
    private Long id;
    @Min(value = 1, message = "Minimum quantity is 1")
    @Max(value = 999, message = "Maximum quantity is 999")
    private int quantity;
}
