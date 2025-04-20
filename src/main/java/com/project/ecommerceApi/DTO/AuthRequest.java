package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "Email required")
    private String email;
    @NotBlank(message = "Password required")
    @Size(min = 12, message = "Password too long")
    @Size(max = 20, message = "Password too long")
    private String password;
}
