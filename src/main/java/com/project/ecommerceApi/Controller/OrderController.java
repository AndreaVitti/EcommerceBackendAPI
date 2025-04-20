package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.ItemsRequested;
import com.project.ecommerceApi.DTO.OrderRequest;
import com.project.ecommerceApi.DTO.PaymentRequest;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.OrderService;
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

    @PostMapping("/add/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> addOrder(@PathVariable("userId") Long userId,
                                             @RequestBody OrderRequest orderRequest) {
        Response response = orderService.addOrder(userId, orderRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> checkoutOrder(@RequestBody PaymentRequest paymentRequest) {
        Response response = orderService.checkoutOrder(paymentRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/addItemToOrder/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> addItemToOrder(@PathVariable("id") Long id, @RequestBody ItemsRequested itemsRequested) {
        Response response = orderService.addItemToOrder(id, itemsRequested);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/removeItemToOrder/{orderId}/{itemId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> removeOrderedProduct(@PathVariable("orderId") Long orderId, @PathVariable("itemId") Long itemId) {
        Response response = orderService.removeOrderedProduct(orderId, itemId);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllOrders() {
        Response response = orderService.getAllOrders();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getOrderById(@PathVariable("id") Long id) {
        Response response = orderService.getOrderById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getProductsById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getProductsByOrderId(@PathVariable("id") Long id) {
        Response response = orderService.getProductsByOrderId(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> deleteOrder(@PathVariable("id") Long id) {
        Response response = orderService.deleteOrder(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
