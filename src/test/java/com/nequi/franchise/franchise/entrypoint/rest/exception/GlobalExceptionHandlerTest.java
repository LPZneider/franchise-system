package com.nequi.franchise.franchise.entrypoint.rest.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleFranchiseNotFound_ShouldReturnNotFoundResponse() {
        // Given
        String errorMessage = "Franchise with id 'franchise-1' not found";
        FranchiseNotFoundException exception = new FranchiseNotFoundException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleFranchiseNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void handleBranchNotFound_ShouldReturnNotFoundResponse() {
        // Given
        String errorMessage = "Branch with id 'branch-1' not found";
        BranchNotFoundException exception = new BranchNotFoundException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBranchNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void handleProductNotFound_ShouldReturnNotFoundResponse() {
        // Given
        String errorMessage = "Product with id 'product-1' not found";
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleProductNotFound(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerErrorResponse() {
        // Given
        Exception exception = new RuntimeException("Unexpected error occurred");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneric(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    void handleGenericException_WithNullPointerException_ShouldReturnInternalServerErrorResponse() {
        // Given
        Exception exception = new NullPointerException("Null value encountered");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneric(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    void handleGenericException_WithIllegalArgumentException_ShouldReturnInternalServerErrorResponse() {
        // Given
        Exception exception = new IllegalArgumentException("Invalid argument provided");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneric(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals(500, response.getBody().getStatus());
    }

    @Test
    void allExceptionHandlers_ShouldReturnValidErrorResponse() {
        // Test that all handlers return non-null ResponseEntity with non-null body
        
        // Given
        FranchiseNotFoundException franchiseEx = new FranchiseNotFoundException("Test");
        BranchNotFoundException branchEx = new BranchNotFoundException("Test");
        ProductNotFoundException productEx = new ProductNotFoundException("Test");
        Exception genericEx = new Exception("Test");

        // When & Then
        ResponseEntity<ErrorResponse> franchiseResponse = globalExceptionHandler.handleFranchiseNotFound(franchiseEx);
        ResponseEntity<ErrorResponse> branchResponse = globalExceptionHandler.handleBranchNotFound(branchEx);
        ResponseEntity<ErrorResponse> productResponse = globalExceptionHandler.handleProductNotFound(productEx);
        ResponseEntity<ErrorResponse> genericResponse = globalExceptionHandler.handleGeneric(genericEx);

        // Assert all responses are valid
        assertNotNull(franchiseResponse);
        assertNotNull(franchiseResponse.getBody());
        
        assertNotNull(branchResponse);
        assertNotNull(branchResponse.getBody());
        
        assertNotNull(productResponse);
        assertNotNull(productResponse.getBody());
        
        assertNotNull(genericResponse);
        assertNotNull(genericResponse.getBody());
    }
}
