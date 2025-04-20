package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("getById/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Response> getUserById(@PathVariable("id") Long id){
        Response response = userService.getUserById(id);
        return  ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getOrdersById/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Response> getOrdersById(@PathVariable("id") Long id){
        Response response = userService.getOrdersById(id);
        return  ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getAddressesById/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public ResponseEntity<Response> getAddressesById(@PathVariable("id") Long id){
        Response response = userService.getAddressesById(id);
        return  ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/deleteUserById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser (@PathVariable("id") Long id){
        Response response = userService.deleteUser(id);
        return  ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
