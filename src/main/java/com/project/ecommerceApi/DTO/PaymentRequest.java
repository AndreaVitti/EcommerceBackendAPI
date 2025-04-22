package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Order required")
    private OrderDTO orderDTO;
    @NotBlank(message = "Payment currency required")
    private String currency;
}
