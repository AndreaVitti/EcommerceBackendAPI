package com.project.ecommerceApi.Service.Impl;


import com.project.ecommerceApi.AuthConfig.JwtService;
import com.project.ecommerceApi.DTO.AuthRequest;
import com.project.ecommerceApi.DTO.RegisterRequest;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Entity.Token;
import com.project.ecommerceApi.Entity.User;
import com.project.ecommerceApi.Exception.GeneralUseException;
import com.project.ecommerceApi.Repository.TokenRepository;
import com.project.ecommerceApi.Repository.UserRepository;
import com.project.ecommerceApi.Service.AuthenticationService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Override
    public Response register(RegisterRequest regRequest) {
        User user = new User();
        user.setFirstname(regRequest.getFirstname());
        user.setLastname(regRequest.getLastname());
        user.setEmail(regRequest.getEmail());

        /*Encode the password*/
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));
        user.setPhone(regRequest.getPhone());
        user.setRoles(regRequest.getRoles());
        userRepository.save(user);

        /*Generate both refresh and access tokens*/
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        /*Generate and store in the table the objects containing the tokens*/
        createTokenObj(jwt, user);
        createTokenObj(refreshToken, user);
        Response response = new Response();
        response.setHttpCode(200);
        response.setToken(jwt);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public Response authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getPassword())
        );
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();

        /*Generate both refresh and access tokens*/
        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        /*Invalidate older tokens*/
        disableAllToken(user);

        /*Generate and store in the table the objects containing the tokens*/
        createTokenObj(jwt, user);
        createTokenObj(refreshToken, user);
        Response response = new Response();
        response.setHttpCode(200);
        response.setToken(jwt);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public Response refreshToken(HttpServletRequest refreshRequest, HttpServletResponse refreshResponse) {
        Response response = new Response();
        final String authHeader = refreshRequest.getHeader("Authorization");
        final String refreshJWToken;
        final String userEmail;

        /*Checks if the header is valid*/
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setHttpCode(403);
            response.setMessage("Unauthorized");
            return response;
        }
        refreshJWToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshJWToken);
        if (userEmail != null) {
            User user;

            /*Checks if the user exists*/
            try {
                user = userRepository.findByEmail(userEmail).orElseThrow(() -> new GeneralUseException("User not found"));
            } catch (GeneralUseException e) {
                response.setHttpCode(404);
                response.setMessage(e.getMessage());
                return response;
            }

            /*Checks if the refresh token is valid*/
            if (jwtService.isTokenValid(refreshJWToken, user)) {

                /*Generate both access and refresh tokens*/
                String JwtToken = jwtService.generateToken(user);
                String newRefreshToken = jwtService.generateRefreshToken(user);

                /*Invalidate older tokens*/
                disableAllToken(user);

                /*Generate and store in the table the objects containing the tokens*/
                createTokenObj(JwtToken, user);
                createTokenObj(newRefreshToken, user);
                response.setHttpCode(200);
                response.setToken(JwtToken);
                response.setMessage(newRefreshToken);
                return response;
            }
        }
        response.setHttpCode(403);
        response.setMessage("Unauthorized");
        return response;
    }

    /*Invalidate all previously stored tokens*/
    private void disableAllToken(User user) {
        List<Token> tokens = tokenRepository.findTokenByUserId(user.getId());
        if (!tokens.isEmpty()) {
            tokens.forEach(t -> t.setLogout(true));
        }
        tokenRepository.saveAll(tokens);
    }

    /*Generate and store in the table the objects containing the tokens*/
    private void createTokenObj(String jwt, User user) {
        Token jwtToken = new Token();
        jwtToken.setJwtToken(jwt);
        jwtToken.setLogout(false);
        jwtToken.setUser(user);
        tokenRepository.save(jwtToken);
    }

    /*Scheduled task that removes invalid tokens from the table*/
    @Transactional
    @Scheduled(fixedDelayString = "${scheduledTask.delay}")
    public void deleteExpiredTokens() {
        List<Token> tokens = tokenRepository.findAll();
        tokens.forEach(t -> {
            try {
                jwtService.isTokenExpired(t.getJwtToken());
            } catch (ExpiredJwtException e) {
                tokenRepository.deleteByJwtToken(t.getJwtToken());
            }
        });
    }
}
