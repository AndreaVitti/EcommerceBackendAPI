package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderCode;
    private LocalDateTime creationDate;
    @DecimalMin(value = "0.5", message = "Minimum price is 0.5")
    @DecimalMax(value = "900000.0", message = "Maximum price is 900000.0")
    private double priceTotal;
    private String status;
    private List<OrderedProductDTO> orderedProductDTOS;
    private UserDTO userDTO;
}
