package com.project.ecommerceApi.Service.Impl;

import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Entity.User;
import com.project.ecommerceApi.Exception.GeneralUseException;
import com.project.ecommerceApi.Repository.UserRepository;
import com.project.ecommerceApi.Service.UserCheckService;
import com.project.ecommerceApi.Service.UserService;
import com.project.ecommerceApi.Utility.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserCheckService userCheckService;

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            response.setHttpCode(404);
            response.setMessage("No user found");
        } else {
            response.setHttpCode(200);
            response.setUserDTOS(Mapper.mapUserListToUserDTOList(users));
        }
        return response;
    }

    @Override
    public Response getUserById(Long id) {
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setUserDTO(Mapper.mapUserToUserDTO(response.getUser()));
            response.setUser(null);
        }
        return response;
    }

    @Override
    public Response getOrdersById(Long id) {
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setUserDTO(Mapper.mapUserToUserDTOPlusOrderDTOSOrAddressesDTOS(response.getUser(), true));
            response.setUser(null);
        }
        return response;
    }

    @Override
    public Response getAddressesById(Long id) {
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setUserDTO(Mapper.mapUserToUserDTOPlusOrderDTOSOrAddressesDTOS(response.getUser(), false));
            response.setUser(null);
        }
        return response;
    }

    @Override
    public Response deleteUser(Long id) {
        Response response = new Response();
        try {
            userRepository.findById(id).orElseThrow(() -> new GeneralUseException("User " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        response.setHttpCode(200);
        userRepository.deleteById(id);
        return response;
    }

    private Response isValidSearch(Long id) {
        Response response = new Response();
        User user;
        try {
            user = userRepository.findById(id).orElseThrow(() -> new GeneralUseException("User " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        if (!userCheckService.checkIfCurrentUserIsAdmin() && !SecurityContextHolder.getContext().getAuthentication().getName().equals(user.getEmail())) {
            response.setHttpCode(403);
            response.setMessage("User not authorized");
            return response;
        }
        response.setHttpCode(200);
        response.setUser(user);
        return response;
    }
}
