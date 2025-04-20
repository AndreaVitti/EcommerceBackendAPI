package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.DTO.Response;

public interface UserService {
    Response getAllUsers();

    Response getUserById(Long id);

    Response getOrdersById(Long id);

    Response getAddressesById(Long id);

    Response deleteUser(Long id);
}
