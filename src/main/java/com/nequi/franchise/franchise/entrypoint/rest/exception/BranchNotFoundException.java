package com.nequi.franchise.franchise.entrypoint.rest.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
