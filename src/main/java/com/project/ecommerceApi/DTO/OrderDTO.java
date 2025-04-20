package com.project.ecommerceApi.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderCode;
    private LocalDateTime creationDate;
    private double priceTotal;
    private String status;
    private List<OrderedProductDTO> orderedProductDTOS;
    private UserDTO userDTO;
}
