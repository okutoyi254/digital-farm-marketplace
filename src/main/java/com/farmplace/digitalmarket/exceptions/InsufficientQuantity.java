package com.farmplace.digitalmarket.exceptions;

public class InsufficientQuantity extends RuntimeException {
    public InsufficientQuantity(String message) {
        super(message);
    }
}
