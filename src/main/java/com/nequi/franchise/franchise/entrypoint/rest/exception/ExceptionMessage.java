package com.nequi.franchise.franchise.entrypoint.rest.exception;

public enum ExceptionMessage {
    FRANCHISE_NOT_FOUND("Franchise not found"),
    BRANCH_NOT_FOUND("Branch not found"),
    PRODUCT_NOT_FOUND("Product not found");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

