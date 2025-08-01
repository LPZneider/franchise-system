package com.nequi.franchise.franchise.entrypoint.rest.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
