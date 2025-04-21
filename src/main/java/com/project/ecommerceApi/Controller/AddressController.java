package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.AddressDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    /*Add a address to a user*/
    @PostMapping("/add/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> addAddressToUser(@PathVariable("userId") Long userId, @RequestBody AddressDTO addressDTO) {
        Response response = addressService.addAddressToUser(userId, addressDTO);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get all addresses*/
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllAddresses() {
        Response response = addressService.getAllAddresses();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get a specific address by its id*/
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> getAddressesById(@PathVariable("id") Long id) {
        Response response = addressService.getAddressesById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Delete an address*/
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> deleteAddress(@PathVariable("id") Long id) {
        Response response = addressService.deleteAddress(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Update the info of an address*/
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> updateAddress(@PathVariable("id") Long id,
                                                  @RequestParam(required = false) String addressName,
                                                  @RequestParam(required = false) String city,
                                                  @RequestParam(required = false) String province,
                                                  @RequestParam(required = false) String country) {
        Response response = addressService.updateAddress(id, addressName, city, province, country);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
