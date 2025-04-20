package com.project.ecommerceApi.Utility;

import com.project.ecommerceApi.DTO.*;
import com.project.ecommerceApi.Entity.*;

import java.util.List;

public class Mapper {
    public static UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }

    public static AddressDTO mapAddressToAddressDTO(Address address) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(address.getId());
        addressDTO.setAddressName(address.getAddressName());
        addressDTO.setCity(address.getCity());
        addressDTO.setProvince(address.getProvince());
        addressDTO.setCountry(address.getCountry());
        return addressDTO;
    }

    public static OrderDTO mapOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderCode(order.getOrderCode());
        orderDTO.setCreationDate(order.getCreationDate());
        orderDTO.setPriceTotal(order.getPriceTotal());
        orderDTO.setStatus(order.getStatus());
        return orderDTO;
    }

    public static ProductDTO mapProductToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());
        productDTO.setImageType(product.getImageType());
        productDTO.setImageData(product.getImageData());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    public static OrderedProductDTO mapOrderedProductToOrderedProductDTO(OrderedProduct orderedProduct) {
        OrderedProductDTO orderedProductDTO = new OrderedProductDTO();
        orderedProductDTO.setId(orderedProduct.getId());
        orderedProductDTO.setQuantity(orderedProduct.getQuantity());
        return orderedProductDTO;
    }

    public static CategoryDTO mapCategoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setCategoryName(category.getCategoryName());
        return categoryDTO;
    }

    public static UserDTO mapUserToUserDTOPlusOrderDTOSOrAddressesDTOS(User user, boolean flag) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setRoles(user.getRoles());
        if (flag) {
            userDTO.setOrderDTOS(Mapper.mapOrderListToOrderDTOList(user.getOrders()));
        } else {
            userDTO.setAddressDTOS(Mapper.mapAddressListToAddressDTOList(user.getAddresses()));
        }
        return userDTO;
    }

    public static CategoryDTO mapCategoryToCategoryDTOPlusProductsDTOS(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setProductDTOS(Mapper.mapProductListToProductDTOList(category.getProducts()));
        return categoryDTO;
    }

    public static ProductDTO mapProductToProductDTOPlusOrderDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());
        productDTO.setImageType(product.getImageType());
        productDTO.setImageData(product.getImageData());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setPrice(product.getPrice());
        productDTO.setOrderedProductDTOS(Mapper.mapOrderedProductListToOrderedProductDTOListPlusOrderDTO(product.getOrderedProducts()));
        return productDTO;
    }

    public static List<OrderedProductDTO> mapOrderedProductListToOrderedProductDTOListPlusOrderDTO(List<OrderedProduct> orderedProducts) {
        return orderedProducts.stream().map(orderedProduct -> {
            OrderedProductDTO orderedProductDTO = Mapper.mapOrderedProductToOrderedProductDTO(orderedProduct);
            orderedProductDTO.setOrderDTO(Mapper.mapOrderToOrderDTO(orderedProduct.getOrder()));
            return orderedProductDTO;
        }).toList();
    }

    public static OrderDTO mapOrderToOrderDTOPlusProductsDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderCode(order.getOrderCode());
        orderDTO.setCreationDate(order.getCreationDate());
        orderDTO.setPriceTotal(order.getPriceTotal());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderedProductDTOS(Mapper.mapOrderedProductListToOrderedProductDTOListPlusProductDTO(order.getOrderedProducts()));
        return orderDTO;
    }

    public static List<OrderedProductDTO> mapOrderedProductListToOrderedProductDTOListPlusProductDTO(List<OrderedProduct> orderedProducts) {
        return orderedProducts.stream().map(orderedProduct -> {
            OrderedProductDTO orderedProductDTO = Mapper.mapOrderedProductToOrderedProductDTO(orderedProduct);
            orderedProductDTO.setProductDTO(Mapper.mapProductToProductDTO(orderedProduct.getProduct()));
            return orderedProductDTO;
        }).toList();
    }

    public static List<UserDTO> mapUserListToUserDTOList(List<User> users) {
        return users.stream().map(user -> Mapper.mapUserToUserDTO(user)).toList();
    }

    public static List<AddressDTO> mapAddressListToAddressDTOList(List<Address> addresses) {
        return addresses.stream().map(address -> Mapper.mapAddressToAddressDTO(address)).toList();
    }

    public static List<OrderDTO> mapOrderListToOrderDTOList(List<Order> orders) {
        return orders.stream().map(order -> Mapper.mapOrderToOrderDTO(order)).toList();
    }

    public static List<ProductDTO> mapProductListToProductDTOList(List<Product> products) {
        return products.stream().map(product -> Mapper.mapProductToProductDTO(product)).toList();
    }

    public static List<CategoryDTO> mapCategoryListToCategoryDTOList(List<Category> categories) {
        return categories.stream().map(category -> Mapper.mapCategoryToCategoryDTO(category)).toList();
    }
}
