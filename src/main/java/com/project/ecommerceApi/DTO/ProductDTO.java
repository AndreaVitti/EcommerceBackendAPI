package com.project.ecommerceApi.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String imageName;
    private String imageType;
    private byte[] imageData;
    private int stockQuantity;
    private double price;
    private CategoryDTO categoryDTO;
    private List<OrderedProductDTO> orderedProductDTOS;
}
