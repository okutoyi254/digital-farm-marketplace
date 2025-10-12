package com.farmplace.digitalmarket.exceptions;

public class FailedToFetchCustomerCart extends RuntimeException {
    public FailedToFetchCustomerCart(String message) {
        super(message);
    }
}
