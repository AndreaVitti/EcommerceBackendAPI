package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.ItemsRequested;
import com.project.ecommerceApi.DTO.OrderRequest;
import com.project.ecommerceApi.DTO.PaymentRequest;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /*Add an order to a user*/
    @PostMapping("/add/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> addOrder(@PathVariable("userId") Long userId,
                                             @Valid @RequestBody OrderRequest orderRequest) {
        Response response = orderService.addOrder(userId, orderRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Request the payment of an order*/
    @PostMapping("/checkout")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> checkoutOrder(@Valid @RequestBody PaymentRequest paymentRequest) {
        Response response = orderService.checkoutOrder(paymentRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Add a product to a pending order*/
    @PostMapping("/addItemToOrder/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> addItemToOrder(@PathVariable("id") Long id, @Valid @RequestBody ItemsRequested itemsRequested) {
        Response response = orderService.addItemToOrder(id, itemsRequested);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Remove a product from a pending order*/
    @DeleteMapping("/removeItemToOrder/{orderId}/{itemId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> removeOrderedProduct(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {
        Response response = orderService.removeOrderedProduct(orderId, itemId);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get all orders*/
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllOrders() {
        Response response = orderService.getAllOrders();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get a specific order by its id*/
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getOrderById(@PathVariable("id") Long id) {
        Response response = orderService.getOrderById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get all products of an order*/
    @GetMapping("/getProductsById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getProductsByOrderId(@PathVariable("id") Long id) {
        Response response = orderService.getProductsByOrderId(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Delete an order*/
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> deleteOrder(@PathVariable("id") Long id) {
        Response response = orderService.deleteOrder(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
