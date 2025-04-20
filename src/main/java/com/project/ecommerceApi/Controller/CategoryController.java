package com.project.ecommerceApi.Controller;

import com.project.ecommerceApi.DTO.CategoryDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addCategory(@RequestBody CategoryDTO categoryDTO) {
        Response response = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAllCategories() {
        Response response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getByName/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getByName(@PathVariable("name") String name) {
        Response response = categoryService.getByName(name);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getAllProducts/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAllProducts(@PathVariable("name") String name) {
        Response response = categoryService.getAllProducts(name);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable("id") Long id) {
        Response response = categoryService.deleteCategory(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateCategory(@PathVariable("id") Long id, @RequestParam(required = false) String name) {
        Response response = categoryService.updateCategory(id, name);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
