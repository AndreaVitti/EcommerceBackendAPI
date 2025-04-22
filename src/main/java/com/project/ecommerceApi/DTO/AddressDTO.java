package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    @NotBlank(message = "Address name required")
    private String addressName;
    @NotBlank(message = "City required")
    private String city;
    @NotBlank(message = "Province required")
    private String province;
    @NotBlank(message = "Country required")
    private String country;
    private UserDTO userDTO;
}
