package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    @NotBlank(message = "Category name required")
    private String categoryName;
    private List<ProductDTO> productDTOS;
}
