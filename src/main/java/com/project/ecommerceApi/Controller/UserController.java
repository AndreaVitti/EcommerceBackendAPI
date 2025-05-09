package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*Get all users*/
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get a specific user by its id*/
    @GetMapping("getById/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Response> getUserById(@PathVariable("id") Long id) {
        Response response = userService.getUserById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get the orders history of a user*/
    @GetMapping("/getOrdersById/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Response> getOrdersById(@PathVariable("id") Long id) {
        Response response = userService.getOrdersById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get all the addresses of a user*/
    @GetMapping("/getAddressesById/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Response> getAddressesById(@PathVariable("id") Long id) {
        Response response = userService.getAddressesById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Delete a user*/
    @DeleteMapping("/deleteUserById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable("id") Long id) {
        Response response = userService.deleteUser(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
