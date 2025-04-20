package com.project.ecommerceApi.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String categoryName;
    private List<ProductDTO> productDTOS;
}
