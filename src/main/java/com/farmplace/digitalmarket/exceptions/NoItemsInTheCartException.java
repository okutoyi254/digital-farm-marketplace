package com.farmplace.digitalmarket.exceptions;

public class NoItemsInTheCartException extends RuntimeException {
    public NoItemsInTheCartException(String message) {
        super(message);
    }
}
