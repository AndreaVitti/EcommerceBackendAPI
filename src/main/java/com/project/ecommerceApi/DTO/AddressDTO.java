package com.project.ecommerceApi.DTO;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String addressName;
    private String city;
    private String province;
    private String country;
    private UserDTO userDTO;
}
