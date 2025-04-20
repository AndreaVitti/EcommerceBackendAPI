package com.project.ecommerceApi.DTO;

import com.project.ecommerceApi.Type.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private List<Role> roles;
    private List<AddressDTO> addressDTOS;
    private List<OrderDTO> orderDTOS;
}
