package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateNameRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;


@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FranchiseHandlerTest {
    @Autowired
    private WebTestClient client;

    private CreateFranchiseRequest createFranchiseRequest;
    private CreateBranchRequest createBranchRequest;
    private UpdateNameRequest updateNameRequest;

    @BeforeEach
    void setUp() {
        createFranchiseRequest = new CreateFranchiseRequest("Test Franchise");
        createBranchRequest = new CreateBranchRequest("Test Branch");
        updateNameRequest = new UpdateNameRequest("Updated Franchise Name");
    }

    @Test
    @DisplayName("Should create franchise successfully - Functional Point 1")
    void shouldCreateFranchise() {
        client.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseResponse.class)
                .value(response -> {
                    assert response.getName().equals("Test Franchise");
                    assert response.getId() != null;
                    assert response.getBranches() != null;
                });
    }

    @Test
    @DisplayName("Should get franchise by id successfully")
    void shouldGetFranchiseById() {
        // First create a franchise
        FranchiseResponse createdFranchise = client.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .returnResult()
                .getResponseBody();

        // Then get it by ID
        client.get()
                .uri("/api/v1/franchises/{id}", createdFranchise.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseResponse.class)
                .value(response -> {
                    assert response.getId().equals(createdFranchise.getId());
                    assert response.getName().equals("Test Franchise");
                });
    }

    @Test
    @DisplayName("Should add branch to franchise successfully - Functional Point 2")
    void shouldAddBranchToFranchise() {
        // First create a franchise
        FranchiseResponse createdFranchise = client.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .returnResult()
                .getResponseBody();

        // Then add a branch
        client.post()
                .uri("/api/v1/franchises/{id}/branches", createdFranchise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createBranchRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BranchResponse.class)
                .value(response -> {
                    assert response.getName().equals("Test Branch");
                    assert response.getId() != null;
                    assert response.getFranchiseId().equals(createdFranchise.getId());
                    assert response.getProducts() != null;
                });
    }

    @Test
    @DisplayName("Should update franchise name successfully - Plus Point")
    void shouldUpdateFranchiseName() {
        // First create a franchise
        FranchiseResponse createdFranchise = client.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .returnResult()
                .getResponseBody();

        // Then update its name
        client.patch()
                .uri("/api/v1/franchises/{id}", createdFranchise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateNameRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(FranchiseResponse.class)
                .value(response -> {
                    assert response.getId().equals(createdFranchise.getId());
                    assert response.getName().equals("Updated Franchise Name");
                });
    }

    @Test
    @DisplayName("Should return 404 when franchise not found")
    void shouldReturn404WhenFranchiseNotFound() {
        client.get()
                .uri("/api/v1/franchises/non-existent-id")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return 404 when adding branch to non-existent franchise")
    void shouldReturn404WhenAddingBranchToNonExistentFranchise() {
        client.post()
                .uri("/api/v1/franchises/non-existent-id/branches")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createBranchRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
}
