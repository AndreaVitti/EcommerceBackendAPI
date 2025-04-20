package com.project.ecommerceApi.Controller;


import com.project.ecommerceApi.DTO.AuthRequest;
import com.project.ecommerceApi.DTO.RegisterRequest;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody RegisterRequest regRequest) {
        Response response = authenticationService.register(regRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@Valid @RequestBody AuthRequest authRequest) {
        Response response = authenticationService.authenticate(authRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Response> refreshToken(HttpServletRequest refreshRequest, HttpServletResponse refreshResponse) {
        Response response = authenticationService.refreshToken(refreshRequest, refreshResponse);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
