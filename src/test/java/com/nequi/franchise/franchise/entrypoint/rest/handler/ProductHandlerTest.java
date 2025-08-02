package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.TopProductResponse;
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
class ProductHandlerTest {

    @Autowired
    private WebTestClient client;

    private CreateFranchiseRequest createFranchiseRequest;
    private CreateBranchRequest createBranchRequest1;
    private CreateBranchRequest createBranchRequest2;
    private CreateProductRequest createProductRequest1;
    private CreateProductRequest createProductRequest2;
    private UpdateNameRequest updateNameRequest;

    @BeforeEach
    void setUp() {
        createFranchiseRequest = new CreateFranchiseRequest("Test Franchise");
        createBranchRequest1 = new CreateBranchRequest("Branch A");
        createBranchRequest2 = new CreateBranchRequest("Branch B");
        createProductRequest1 = new CreateProductRequest("Product A", 150);
        createProductRequest2 = new CreateProductRequest("Product B", 200);
        updateNameRequest = new UpdateNameRequest("Updated Product Name");
    }

    @Test
    @DisplayName("Should get top stock products by branch successfully - Functional Point 6")
    void shouldGetTopStockProductsByBranch() {
        // Setup: Create franchise with multiple branches and products
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch1 = createTestBranch(franchise.getId(), createBranchRequest1);
        BranchResponse branch2 = createTestBranch(franchise.getId(), createBranchRequest2);

        // Add products with different stock levels
        createTestProduct(franchise.getId(), branch1.getId(), createProductRequest1); // Stock: 150
        createTestProduct(franchise.getId(), branch2.getId(), createProductRequest2); // Stock: 200

        // Get top stock products
        client.get()
                .uri("/api/v1/franchises/{franchiseId}/top-stock-products", franchise.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(TopProductResponse.class)
                .value(response -> {
                    assert !response.isEmpty();
                    // Verify that each response contains required fields
                    response.forEach(product -> {
                        assert product.getBranchId() != null;
                        assert product.getProductId() != null;
                        assert product.getProductName() != null;
                        assert product.getStock() > 0;
                    });
                });
    }

    @Test
    @DisplayName("Should update product name successfully - Plus Point")
    void shouldUpdateProductName() {
        // Setup: Create franchise, branch, and product
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId(), createBranchRequest1);
        ProductResponse product = createTestProduct(franchise.getId(), branch.getId(), createProductRequest1);

        // Update product name
        client.patch()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products/{productId}",
                     franchise.getId(), branch.getId(), product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateNameRequest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ProductResponse.class)
                .value(response -> {
                    assert response.getId().equals(product.getId());
                    assert response.getName().equals("Updated Product Name");
                    assert response.getBranchId().equals(branch.getId());
                });
    }

    @Test
    @DisplayName("Should return empty list when franchise has no products")
    void shouldReturnEmptyListWhenFranchiseHasNoProducts() {
        // Create franchise without products
        FranchiseResponse franchise = createTestFranchise();

        client.get()
                .uri("/api/v1/franchises/{franchiseId}/top-stock-products", franchise.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(TopProductResponse.class)
                .value(response -> {
                    assert response.isEmpty();
                });
    }

    @Test
    @DisplayName("Should return 404 when getting top products for non-existent franchise")
    void shouldReturn404WhenGettingTopProductsForNonExistentFranchise() {
        client.get()
                .uri("/api/v1/franchises/non-existent-id/top-stock-products")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should return 404 when updating name of non-existent product")
    void shouldReturn404WhenUpdatingNameOfNonExistentProduct() {
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId(), createBranchRequest1);

        client.patch()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products/non-existent",
                     franchise.getId(), branch.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateNameRequest)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Should handle multiple products with different stock levels")
    void shouldHandleMultipleProductsWithDifferentStockLevels() {
        // Setup: Create franchise with multiple branches and products with varying stock
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch1 = createTestBranch(franchise.getId(), createBranchRequest1);
        BranchResponse branch2 = createTestBranch(franchise.getId(), createBranchRequest2);

        // Create products with different stock levels
        CreateProductRequest highStockProduct = new CreateProductRequest("High Stock Product", 500);
        CreateProductRequest lowStockProduct = new CreateProductRequest("Low Stock Product", 50);

        createTestProduct(franchise.getId(), branch1.getId(), highStockProduct);
        createTestProduct(franchise.getId(), branch2.getId(), lowStockProduct);

        // Verify top stock products ordering
        client.get()
                .uri("/api/v1/franchises/{franchiseId}/top-stock-products", franchise.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopProductResponse.class)
                .value(response -> {
                    assert response.size() == 2;
                    // Verify products are present with correct stock levels
                    boolean hasHighStock = response.stream().anyMatch(p -> p.getStock() == 500);
                    boolean hasLowStock = response.stream().anyMatch(p -> p.getStock() == 50);
                    assert hasHighStock && hasLowStock;
                });
    }

    @Test
    @DisplayName("Should validate top products response structure")
    void shouldValidateTopProductsResponseStructure() {
        // Setup: Create franchise with branch and product
        FranchiseResponse franchise = createTestFranchise();
        BranchResponse branch = createTestBranch(franchise.getId(), createBranchRequest1);
        createTestProduct(franchise.getId(), branch.getId(), createProductRequest1);

        // Verify response structure
        client.get()
                .uri("/api/v1/franchises/{franchiseId}/top-stock-products", franchise.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopProductResponse.class)
                .value(response -> {
                    assert response.size() == 1;
                    TopProductResponse product = response.get(0);
                    assert product.getBranchId().equals(branch.getId());
                    assert product.getProductName().equals("Product A");
                    assert product.getStock() == 150;
                    assert product.getProductId() != null;
                });
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

    private BranchResponse createTestBranch(String franchiseId, CreateBranchRequest branchRequest) {
        return client.post()
                .uri("/api/v1/franchises/{id}/branches", franchiseId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(branchRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BranchResponse.class)
                .returnResult()
                .getResponseBody();
    }

    private ProductResponse createTestProduct(String franchiseId, String branchId, CreateProductRequest productRequest) {
        return client.post()
                .uri("/api/v1/franchises/{franchiseId}/branches/{branchId}/products",
                     franchiseId, branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductResponse.class)
                .returnResult()
                .getResponseBody();
    }
}
