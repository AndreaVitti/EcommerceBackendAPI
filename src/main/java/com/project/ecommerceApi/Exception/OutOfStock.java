package com.project.ecommerceApi.Exception;

public class OutOfStock extends RuntimeException {
    public OutOfStock(String message) {
        super(message);
    }
}
