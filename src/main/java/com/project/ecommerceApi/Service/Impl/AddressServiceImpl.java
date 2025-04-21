package com.project.ecommerceApi.Service.Impl;

import com.project.ecommerceApi.DTO.AddressDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Entity.Address;
import com.project.ecommerceApi.Entity.User;
import com.project.ecommerceApi.Exception.GeneralUseException;
import com.project.ecommerceApi.Repository.AddressRepository;
import com.project.ecommerceApi.Repository.UserRepository;
import com.project.ecommerceApi.Service.AddressService;
import com.project.ecommerceApi.Service.UserCheckService;
import com.project.ecommerceApi.Utility.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserCheckService userCheckService;

    @Override
    public Response addAddressToUser(Long userId, AddressDTO addressDTO) {
        Response response = new Response();
        Address address = new Address();
        User user;

        /*Checks if the user exists*/
        try {
            user = userRepository.findById(userId).orElseThrow(() -> new GeneralUseException("User " + userId + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }

        /*Check if the logged user is accessing its resources or not*/
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userCheckService.checkIfCurrentUserIsAdmin() && !loggedUser.getId().equals(userId)) {
            response.setHttpCode(403);
            response.setMessage("User not authorized");
            return response;
        }

        /*Create and save the address*/
        address.setUser(user);
        address.setAddressName(addressDTO.getAddressName());
        address.setCity(addressDTO.getCity());
        address.setProvince(addressDTO.getProvince());
        address.setCountry(addressDTO.getCountry());
        addressRepository.save(address);
        response.setHttpCode(200);
        return response;
    }

    @Override
    public Response getAllAddresses() {
        Response response = new Response();
        List<Address> addresses = addressRepository.findAll();

        /*Checks if there are addresses or not*/
        if (addresses.isEmpty()) {
            response.setHttpCode(404);
            response.setMessage("No address found");
        } else {
            response.setHttpCode(200);
            response.setAddressDTOS(Mapper.mapAddressListToAddressDTOList(addresses));
        }
        return response;
    }

    @Override
    public Response getAddressesById(Long id) {

        /*Check if the address can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setAddressDTO(Mapper.mapAddressToAddressDTO(response.getAddress()));
            response.setAddress(null);
        }
        return response;
    }

    @Override
    public Response deleteAddress(Long id) {

        /*Checks if the address can be deleted*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setAddress(null);
            addressRepository.deleteById(id);
        }
        return response;
    }

    @Override
    public Response updateAddress(Long id, String addressName, String city, String province, String country) {
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            Address address = response.getAddress();
            response.setAddress(null);

            /*Check if the parameters are valid*/
            if (addressName != null && !address.getAddressName().equals(addressName)) {
                address.setAddressName(addressName);
            }
            if (city != null && !address.getCity().equals(city)) {
                address.setCity(city);
            }
            if (province != null && !address.getProvince().equals(province)) {
                address.setProvince(province);
            }
            if (country != null && !address.getCountry().equals(country)) {
                address.setCountry(country);
            }
            addressRepository.save(address);
            response.setAddressDTO(Mapper.mapAddressToAddressDTO(address));
        }
        return response;
    }

    /*Check if the address is actually stored in the table*/
    private Response isValidSearch(Long id) {
        Response response = new Response();
        Address address;
        try {
            address = addressRepository.findById(id).orElseThrow(() -> new GeneralUseException("Address " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        return supplemetaryValidation(address, response);
    }

    /*Check if the user is accessing its own resources*/
    private Response supplemetaryValidation(Address address, Response response) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userCheckService.checkIfCurrentUserIsAdmin() &&
                !user.getId().equals(address.getUser().getId())) {
            response.setHttpCode(403);
            response.setMessage("User not authorized");
            return response;
        }
        response.setHttpCode(200);
        response.setAddress(address);
        return response;
    }
}
