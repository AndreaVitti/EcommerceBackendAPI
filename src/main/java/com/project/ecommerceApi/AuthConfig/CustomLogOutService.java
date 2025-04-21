package com.project.ecommerceApi.AuthConfig;

import com.project.ecommerceApi.Entity.Token;
import com.project.ecommerceApi.Repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomLogOutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /*Method for user logouts*/
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        /*Check if the header is valid*/
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Token jwtToken = tokenRepository.findByJwtToken(jwt).orElse(null);

        /*If valid set token logout to true*/
        if (jwtToken != null) {
            jwtToken.setLogout(true);
            tokenRepository.save(jwtToken);
        }
    }
}
