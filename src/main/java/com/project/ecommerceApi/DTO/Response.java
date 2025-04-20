package com.project.ecommerceApi.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.ecommerceApi.Entity.*;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int httpCode;
    private String message;

    private String token;
    private String refreshToken;

    private String sessionId;
    private String sessionUrl;

    private User user;
    private Address address;
    private Category category;
    private Product product;
    private Order order;
    private AddressDTO addressDTO;
    private UserDTO userDTO;
    private OrderDTO orderDTO;
    private ProductDTO productDTO;
    private CategoryDTO categoryDTO;
    private List<AddressDTO> addressDTOS;
    private List<UserDTO> userDTOS;
    private List<OrderDTO> orderDTOS;
    private List<ProductDTO> productDTOS;
    private List<CategoryDTO> categoryDTOS;
}
