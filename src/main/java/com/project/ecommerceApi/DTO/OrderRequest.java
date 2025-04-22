package com.project.ecommerceApi.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull(message = "Can't have empty order")
    private List<ItemsRequested> itemsRequestedList;
}
