package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveProductFromBranchUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private RemoveProductFromBranchUseCase removeProductFromBranchUseCase;

    private String franchiseId;
    private String branchId;
    private String productId;
    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        franchiseId = "franchise-1";
        branchId = "branch-1";
        productId = "product-1";
        
        franchise = new Franchise("franchise-1", new Name("Test Franchise"));
        branch = new Branch("branch-1", new Name("Test Branch"));
        product = new Product("product-1", new Name("Test Product"), new Stock(10));
        
        branch.addProduct(product);
        franchise.addBranch(branch);
    }

    @Test
    void execute_ShouldRemoveProductSuccessfully() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        // When
        Mono<Void> result = removeProductFromBranchUseCase.execute(franchiseId, branchId, productId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void execute_WhenFranchiseNotFound_ShouldThrowException() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // When
        Mono<Void> result = removeProductFromBranchUseCase.execute(franchiseId, branchId, productId);

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
        Mono<Void> result = removeProductFromBranchUseCase.execute(franchiseId, "non-existent-branch", productId);

        // Then
        StepVerifier.create(result)
                .expectError(BranchNotFoundException.class)
                .verify();
    }

    @Test
    void execute_WhenProductNotFound_ShouldThrowException() {
        // Given
        Branch emptyBranch = new Branch("branch-1", new Name("Test Branch"));
        Franchise franchiseWithEmptyBranch = new Franchise("franchise-1", new Name("Test Franchise"));
        franchiseWithEmptyBranch.addBranch(emptyBranch);
        
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchiseWithEmptyBranch));

        // When
        Mono<Void> result = removeProductFromBranchUseCase.execute(franchiseId, branchId, "non-existent-product");

        // Then
        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    void execute_WhenRepositoryFails_ShouldPropagateError() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<Void> result = removeProductFromBranchUseCase.execute(franchiseId, branchId, productId);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
