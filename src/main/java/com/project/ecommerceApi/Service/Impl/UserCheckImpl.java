package com.project.ecommerceApi.Service.Impl;

import com.project.ecommerceApi.Entity.User;
import com.project.ecommerceApi.Repository.UserRepository;
import com.project.ecommerceApi.Service.UserCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCheckImpl implements UserCheckService {

    private final UserRepository userRepository;

    @Override
    public boolean checkIfCurrentUserIsAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.toString().equals("ADMIN"));
    }

    @Override
    public User findMyInfo(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
