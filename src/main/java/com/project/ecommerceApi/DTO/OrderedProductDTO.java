package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class OrderedProductDTO {
    private Long id;
    @Min(value = 1, message = "Minimum quantity is 1")
    @Max(value = 999, message = "Maximum quantity is 999")
    private int quantity;
    private ProductDTO productDTO;
    private OrderDTO orderDTO;
}
