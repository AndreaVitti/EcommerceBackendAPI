package com.project.ecommerceApi.Service;

import com.project.ecommerceApi.DTO.ItemsRequested;
import com.project.ecommerceApi.DTO.OrderRequest;
import com.project.ecommerceApi.DTO.PaymentRequest;
import com.project.ecommerceApi.DTO.Response;

public interface OrderService {
    Response addOrder(Long userId, OrderRequest orderRequest);

    Response getAllOrders();

    Response checkoutOrder(PaymentRequest paymentRequest);

    Response addItemToOrder(Long id, ItemsRequested itemsRequested);

    Response removeOrderedProduct(Long orderId, Long itemID);

    Response getOrderById(Long id);

    Response getProductsByOrderId(Long id);

    Response deleteOrder(Long id);
}
