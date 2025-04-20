package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.DTO.CategoryDTO;
import com.project.ecommerceApi.DTO.Response;

public interface CategoryService {

    Response addCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getByName(String name);

    Response getAllProducts(String name);

    Response deleteCategory(Long id);

    Response updateCategory(Long id, String name);
}
