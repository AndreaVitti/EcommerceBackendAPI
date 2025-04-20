package com.project.ecommerceApi.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<ItemsRequested> itemsRequestedList;
}
