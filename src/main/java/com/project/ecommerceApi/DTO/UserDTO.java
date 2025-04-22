package com.project.ecommerceApi.DTO;

import com.project.ecommerceApi.Type.Role;
import com.project.ecommerceApi.Validation.PhoneIsValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "First name required")
    private String firstname;
    @NotBlank(message = "Last name required")
    private String lastname;
    @Email(message = "Email required")
    private String email;
    @PhoneIsValid(message = "Phone number is requires")
    private String phone;
    private List<Role> roles;
    private List<AddressDTO> addressDTOS;
    private List<OrderDTO> orderDTOS;
}
