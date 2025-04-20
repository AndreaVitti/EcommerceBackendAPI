package com.project.ecommerceApi.DTO;

import lombok.Data;

@Data
public class OrderedProductDTO {
    private int quantity;
    private Long id;
    private ProductDTO productDTO;
    private OrderDTO orderDTO;
}
