package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductToBranchUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private FranchiseFactory franchiseFactory;

    @InjectMocks
    private AddProductToBranchUseCase addProductToBranchUseCase;

    private String franchiseId;
    private String branchId;
    private CreateProductRequest request;
    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        franchiseId = "franchise-1";
        branchId = "branch-1";
        
        request = new CreateProductRequest();
        request.setName("Test Product");
        request.setStock(10);
        
        franchise = new Franchise("franchise-1", new Name("Test Franchise"));
        branch = new Branch("branch-1", new Name("Test Branch"));
        product = new Product("product-1", new Name("Test Product"), new Stock(10));
        
        franchise.addBranch(branch);
    }

    @Test
    void execute_ShouldAddProductSuccessfully() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseFactory.createProduct(anyString(), anyString(), anyInt()))
                .thenReturn(product);
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        // When
        Mono<ProductResponse> result = addProductToBranchUseCase.execute(franchiseId, branchId, request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    "product-1".equals(response.getId()) &&
                    "Test Product".equals(response.getName()) &&
                    response.getStock() == 10 &&
                    branchId.equals(response.getBranchId())
                )
                .verifyComplete();
    }

    @Test
    void execute_WhenFranchiseNotFound_ShouldThrowException() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // When
        Mono<ProductResponse> result = addProductToBranchUseCase.execute(franchiseId, branchId, request);

        // Then
        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    void execute_WhenBranchNotFound_ShouldThrowException() {
        // Given
        Franchise emptyFranchise = new Franchise("franchise-1", new Name("Test Franchise"));
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(emptyFranchise));

        // When
        Mono<ProductResponse> result = addProductToBranchUseCase.execute(franchiseId, "non-existent-branch", request);

        // Then
        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void execute_WhenRepositoryFails_ShouldPropagateError() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseFactory.createProduct(anyString(), anyString(), anyInt()))
                .thenReturn(product);
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<ProductResponse> result = addProductToBranchUseCase.execute(franchiseId, branchId, request);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
