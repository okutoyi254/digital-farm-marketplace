package com.farmplace.digitalmarket.exceptions;

public class ProductDoesntExistException extends RuntimeException {
    public ProductDoesntExistException(String message) {
        super(message);
    }
}
