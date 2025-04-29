package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.ProductDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.ProductService;
import jakarta.validation.Valid;
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

    /*Add a product to a category*/
    @PostMapping("/addToCategory/{category}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addProduct(@PathVariable("category") String categoryName, @Valid @RequestPart("data") ProductDTO productDTO, @RequestPart("image") MultipartFile imageFile) {
        Response response;
        try {
            response = productService.addProduct(categoryName, productDTO, imageFile);
        } catch (IOException e) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get all products*/
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAllProducts() {
        Response response = productService.getAllProducts();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get a specific product by its id*/
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getProductById(@PathVariable("id") Long id) {
        Response response = productService.getProductById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Get all the orders containing a certain product*/
    @GetMapping("/getOrdersById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getOrdersByProductId(@PathVariable("id") Long id) {
        Response response = productService.getOrdersByProductId(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Delete a product*/
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable("id") Long id) {
        Response response = productService.deleteProduct(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    /*Update the info of a product*/
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

    /*Upload a new image of a product*/
    @PutMapping("/uploadImage/{id}")
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
