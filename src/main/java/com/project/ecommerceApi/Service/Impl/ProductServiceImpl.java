package com.project.ecommerceApi.Service.Impl;

import com.project.ecommerceApi.DTO.ProductDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Entity.Category;
import com.project.ecommerceApi.Entity.Product;
import com.project.ecommerceApi.Exception.GeneralUseException;
import com.project.ecommerceApi.Repository.CategoryRepository;
import com.project.ecommerceApi.Repository.ProductRepository;
import com.project.ecommerceApi.Service.ProductService;
import com.project.ecommerceApi.Utility.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Response addProduct(String categoryName, ProductDTO productDTO, MultipartFile imageFile) throws IOException {
        Response response = new Response();
        Product product = new Product();
        Category category;

        /*Check if the category exists*/
        try {
            category = categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new GeneralUseException("Category " + categoryName + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);
        productRepository.save(product);
        response.setHttpCode(200);
        return response;
    }

    @Override
    public Response getAllProducts() {
        Response response = new Response();

        /*Check if there are categories or not*/
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            response.setHttpCode(404);
            response.setMessage("No product found");
        } else {
            response.setHttpCode(200);
            response.setProductDTOS(Mapper.mapProductListToProductDTOList(products));
        }
        return response;
    }

    @Override
    public Response getProductById(Long id) {

        /*Check if the product can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            response.setProductDTO(Mapper.mapProductToProductDTO(response.getProduct()));
            response.setProduct(null);
        }
        return response;
    }

    @Override
    public Response getOrdersByProductId(Long id) {
        Response response = new Response();
        Product product;

        /*Check if the product exists*/
        try {
            product = productRepository.findById(id).orElseThrow(() -> new GeneralUseException("Product " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        response.setHttpCode(200);
        response.setProductDTO(Mapper.mapProductToProductDTOPlusOrderDTO(product));
        return response;
    }

    @Override
    public Response deleteProduct(Long id) {
        Response response = new Response();

        /*Check if the product exists*/
        try {
            productRepository.findById(id).orElseThrow(() -> new GeneralUseException("Product " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        productRepository.deleteById(id);
        response.setHttpCode(200);
        return response;
    }

    @Override
    public Response updateProduct(Long id, String name, String description, int stockQuantity, double price) {

        /*Check if the product can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() == 200) {
            Product product = response.getProduct();
            response.setProduct(null);

            /*Check if the parameters are valid*/
            if (name != null && !product.getName().equals(name)) {
                product.setName(name);
            }
            if (description != null && !product.getDescription().equals(description)) {
                product.setDescription(description);
            }
            if (stockQuantity != 0 && !(product.getStockQuantity() == stockQuantity)) {
                product.setStockQuantity(stockQuantity);
            }
            if (price != 0 && !(product.getPrice() == price)) {
                product.setPrice(price);
            }
            productRepository.save(product);
            response.setProductDTO(Mapper.mapProductToProductDTO(product));
        }
        return response;
    }

    @Override
    public Response uploadImageById(Long id, MultipartFile imageFile) throws IOException {

        /*Check if the product can be searched*/
        Response response = isValidSearch(id);
        if (response.getHttpCode() != 200) {
            return response;
        }
        Product product = response.getProduct();
        response.setProduct(null);

        /*Check if the parameters are valid*/
        if (imageFile.getOriginalFilename() != null && imageFile.getContentType() != null) {
            product.setImageName(imageFile.getOriginalFilename());
            product.setImageType(imageFile.getContentType());
            product.setImageData(imageFile.getBytes());
            productRepository.save(product);
        }
        response.setProductDTO(Mapper.mapProductToProductDTO(product));
        return response;
    }

    /*Check if the product is actually stored in the table*/
    private Response isValidSearch(Long id) {
        Response response = new Response();
        Product product;
        try {
            product = productRepository.findById(id).orElseThrow(() -> new GeneralUseException("Product " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        response.setHttpCode(200);
        response.setProduct(product);
        return response;
    }
}
