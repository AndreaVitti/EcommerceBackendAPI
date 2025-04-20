package com.project.ecommerceApi.Service.Impl;

import com.project.ecommerceApi.DTO.CategoryDTO;
import com.project.ecommerceApi.DTO.Response;
import com.project.ecommerceApi.Entity.Category;
import com.project.ecommerceApi.Exception.GeneralUseException;
import com.project.ecommerceApi.Repository.CategoryRepository;
import com.project.ecommerceApi.Service.CategoryService;
import com.project.ecommerceApi.Utility.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Response addCategory(CategoryDTO categoryDTO) {
        Response response = new Response();
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        categoryRepository.save(category);
        response.setHttpCode(200);
        return response;
    }

    @Override
    public Response getAllCategories() {
        Response response = new Response();
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            response.setHttpCode(404);
            response.setMessage("No category found");
        } else {
            response.setHttpCode(200);
            response.setCategoryDTOS(Mapper.mapCategoryListToCategoryDTOList(categories));
        }
        return response;
    }

    @Override
    public Response getByName(String name) {
        Response response = isValidSearch(name);
        if (response.getHttpCode() == 200) {
            response.setCategoryDTO(Mapper.mapCategoryToCategoryDTO(response.getCategory()));
            response.setCategory(null);
        }
        return response;
    }

    @Override
    public Response getAllProducts(String name) {
        Response response = isValidSearch(name);
        if (response.getHttpCode() == 200) {
            response.setCategoryDTO(Mapper.mapCategoryToCategoryDTOPlusProductsDTOS(response.getCategory()));
            response.setCategory(null);
        }
        return response;
    }

    @Override
    public Response deleteCategory(Long id) {
        Response response = new Response();
        try {
            categoryRepository.findById(id).orElseThrow(() -> new GeneralUseException("Category " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        response.setHttpCode(200);
        categoryRepository.deleteById(id);
        return response;
    }

    @Override
    public Response updateCategory(Long id, String name) {
        Response response = new Response();
        Category category;
        try {
            category = categoryRepository.findById(id).orElseThrow(() -> new GeneralUseException("Category " + id + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        if (name != null && !category.getCategoryName().equals(name)) {
            category.setCategoryName(name);
            categoryRepository.save(category);
        }
        response.setHttpCode(200);
        response.setCategoryDTO(Mapper.mapCategoryToCategoryDTO(category));
        return response;
    }

    private Response isValidSearch(String name) {
        Response response = new Response();
        Category category;
        try {
            category = categoryRepository.findByCategoryName(name).orElseThrow(() -> new GeneralUseException("Category " + name + " not found"));
        } catch (GeneralUseException e) {
            response.setHttpCode(404);
            response.setMessage(e.getMessage());
            return response;
        }
        response.setHttpCode(200);
        response.setCategory(category);
        return response;
    }
}
