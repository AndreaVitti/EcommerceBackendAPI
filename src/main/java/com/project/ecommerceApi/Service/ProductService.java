package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.DTO.ProductDTO;
import com.project.ecommerceApi.DTO.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    Response addProduct(String categoryName, ProductDTO productDTO, MultipartFile imageFile) throws IOException;

    Response getAllProducts();

    Response getProductById(Long id);

    Response getOrdersByProductId(Long id);

    Response deleteProduct(Long id);

    Response updateProduct(Long id, String name, String description, int stockQuantity, double price);

    Response uploadImageById(Long id, MultipartFile imageFile) throws IOException;
}
