package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.DTO.AddressDTO;
import com.project.ecommerceApi.DTO.Response;

public interface AddressService {

    Response addAddressToUser(Long userId, AddressDTO addressDTO);

    Response getAllAddresses();

    Response getAddressesById(Long id);

    Response deleteAddress(Long id);

    Response updateAddress(Long id, String addressName, String city, String province, String country);
}
