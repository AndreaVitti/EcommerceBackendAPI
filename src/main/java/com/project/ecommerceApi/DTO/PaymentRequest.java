package com.project.ecommerceApi.DTO;

import lombok.Data;

@Data
public class PaymentRequest {
    private OrderDTO orderDTO;
    private String currency;
}
