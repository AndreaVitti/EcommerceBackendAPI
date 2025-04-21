package com.project.ecommerceApi.AuthConfig;

import com.project.ecommerceApi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /*Search for the user based on the username*/
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /*Provides the AuthentificationProvider based on userDetails and passwordEncoder*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider DaoAuthProvider = new DaoAuthenticationProvider();
        DaoAuthProvider.setUserDetailsService(userDetailsService());
        DaoAuthProvider.setPasswordEncoder(passwordEncoder());
        return DaoAuthProvider;
    }

    /*Encode password*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*Provides the authenticationManager*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authcConfig) throws Exception {
        return authcConfig.getAuthenticationManager();
    }
}
