package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.DTO.AuthRequest;
import com.project.ecommerceApi.DTO.RegisterRequest;
import com.project.ecommerceApi.DTO.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthenticationService {

    Response register(RegisterRequest request);

    Response authenticate(AuthRequest request);

    Response refreshToken(HttpServletRequest request, HttpServletResponse response);
}
