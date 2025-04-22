package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Name required")
    private String name;
    private String description;
    private String imageName;
    private String imageType;
    private byte[] imageData;
    @Min(value = 1, message = "Minimum stock is 1")
    @Max(value = 999, message = "Maximum stock is 999")
    private int stockQuantity;
    @DecimalMin(value = "0.5", message = "Minimum price is 0.5")
    @DecimalMax(value = "900000.0", message = "Maximum price is 900000.0")
    private double price;
    private CategoryDTO categoryDTO;
    private List<OrderedProductDTO> orderedProductDTOS;
}
