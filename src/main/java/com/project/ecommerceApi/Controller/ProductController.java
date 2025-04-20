package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.ProductDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/addToCategory/{category}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addProduct(@PathVariable("category") String categoryName, @RequestPart("data") ProductDTO productDTO, @RequestPart("image") MultipartFile imageFile) {
        Response response;
        try {
            response = productService.addProduct(categoryName, productDTO, imageFile);
        } catch (IOException e) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAllProducts() {
        Response response = productService.getAllProducts();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getProductById(@PathVariable("id") Long id) {
        Response response = productService.getProductById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getOrdersById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getOrdersByProductId(@PathVariable("id") Long id) {
        Response response = productService.getOrdersByProductId(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable("id") Long id) {
        Response response = productService.deleteProduct(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(@PathVariable("id") Long id,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false) int stockQuantity,
                                                  @RequestParam(required = false) double price) {
        Response response = productService.updateProduct(id, name, description, stockQuantity, price);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/uploadImage/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> uploadImageById(@PathVariable("id") Long id, @RequestPart("image") MultipartFile imageFile) {
        Response response;
        try {
            response = productService.uploadImageById(id, imageFile);
        } catch (IOException e) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
