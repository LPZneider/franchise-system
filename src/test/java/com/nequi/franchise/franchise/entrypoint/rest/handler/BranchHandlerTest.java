package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateNameRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateStockRequest;
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
class BranchHandlerTest {

    @Autowired
    private WebTestClient client;

    private CreateFranchiseRequest createFranchiseRequest;
    private CreateBranchRequest createBranchRequest;
    private CreateProductRequest createProductRequest;
    private UpdateStockRequest updateStockRequest;
    private UpdateNameRequest updateNameRequest;

    @BeforeEach
    void setUp() {
        createFranchiseRequest = new CreateFranchiseRequest("Test Franchise");
        createBranchRequest = new CreateBranchRequest("Test Branch");
        createProductRequest = new CreateProductRequest("Test Product", 100);
        updateStockRequest = new UpdateStockRequest(150);
        updateNameRequest = new UpdateNameRequest("Updated Branch Name");
    }

    @Test
    @DisplayName("Should add product to branch successfully - Functional Point 3")
    void shouldAddProductToBranch() {
        // First create franchise and branch
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId());

        // Then add product to branch
        client.post()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products",
                     franchise.getId(), branch.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createProductRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductResponse.class)
                .value(response -> {
                    assert response.getName().equals("Test Product");
                    assert response.getStock() == 100;
                    assert response.getId() != null;
                    assert response.getBranchId().equals(branch.getId());
                });
    }

    @Test
    @DisplayName("Should update product stock successfully - Functional Point 5")
    void shouldUpdateProductStock() {
        // Setup: Create franchise, branch, and product
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId());
        ProductResponse product = createTestProduct(franchise.getId(), branch.getId());

        // Update product stock
        client.patch()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock",
                     franchise.getId(), branch.getId(), product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateStockRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductResponse.class)
                .value(response -> {
                    assert response.getId().equals(product.getId());
                    assert response.getStock() == 150;
                    assert response.getBranchId().equals(branch.getId());
                });
    }

    @Test
    @DisplayName("Should remove product from branch successfully - Functional Point 4")
    void shouldRemoveProductFromBranch() {
        // Setup: Create franchise, branch, and product
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId());
        ProductResponse product = createTestProduct(franchise.getId(), branch.getId());

        // Remove product from branch
        client.delete()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}",
                     franchise.getId(), branch.getId(), product.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Should update branch name successfully - Plus Point")
    void shouldUpdateBranchName() {
        // Setup: Create franchise and branch
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId());

        // Update branch name
        client.patch()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}",
                     franchise.getId(), branch.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateNameRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BranchResponse.class)
                .value(response -> {
                    assert response.getId().equals(branch.getId());
                    assert response.getName().equals("Updated Branch Name");
                    assert response.getFranchiseId().equals(franchise.getId());
                });
    }

    @Test
    @DisplayName("Should return 404 when adding product to non-existent franchise")
    void shouldReturn404WhenAddingProductToNonExistentFranchise() {
        client.post()
                .uri("/api/v1/franchises/non-existent/branches/any-branch/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createProductRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return 404 when adding product to non-existent branch")
    void shouldReturn404WhenAddingProductToNonExistentBranch() {
        FranchiseResponse franchise = createTestFranchise();

        client.post()
                .uri("/api/v1/franchises/{franchiseId}/branches/non-existent/products",
                     franchise.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createProductRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    // Helper methods
    private FranchiseResponse createTestFranchise() {
        return client.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createFranchiseRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private BranchResponse createTestBranch(String franchiseId) {
        return client.post()
                .uri("/api/v1/franchises/{id}/branches", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createBranchRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BranchResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private ProductResponse createTestProduct(String franchiseId, String branchId) {
        return client.post()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products",
                     franchiseId, branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createProductRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
