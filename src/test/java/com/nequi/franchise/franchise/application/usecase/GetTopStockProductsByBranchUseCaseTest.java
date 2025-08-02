package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.domain.service.ProductStockService;
import com.nequi.franchise.franchise.entrypoint.rest.dto.TopProductResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTopStockProductsByBranchUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private ProductStockService productStockService;

    @InjectMocks
    private GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase;

    private String franchiseId;
    private Franchise franchise;
    private Branch branch1;
    private Branch branch2;
    private Product product1;
    private Product product2;
    private Map<Branch, Product> topProductsMap;

    @BeforeEach
    void setUp() {
        franchiseId = "franchise-1";
        franchise = new Franchise("franchise-1", new Name("Test Franchise"));
        
        branch1 = new Branch("branch-1", new Name("Branch 1"));
        branch2 = new Branch("branch-2", new Name("Branch 2"));
        
        product1 = new Product("product-1", new Name("Product 1"), new Stock(15));
        product2 = new Product("product-2", new Name("Product 2"), new Stock(25));
        
        topProductsMap = new HashMap<>();
        topProductsMap.put(branch1, product1);
        topProductsMap.put(branch2, product2);
    }

    @Test
    void execute_ShouldReturnTopProductsPerBranch() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(productStockService.findTopStockPerBranch(franchise))
                .thenReturn(topProductsMap);

        // When
        Mono<List<TopProductResponse>> result = getTopStockProductsByBranchUseCase.execute(franchiseId);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(responses -> {
                    if (responses.size() != 2) return false;
                    
                    boolean foundBranch1Product = responses.stream().anyMatch(r ->
                        "branch-1".equals(r.getBranchId()) &&
                        "product-1".equals(r.getProductId()) &&
                        "Product 1".equals(r.getProductName()) &&
                        r.getStock() == 15
                    );
                    
                    boolean foundBranch2Product = responses.stream().anyMatch(r ->
                        "branch-2".equals(r.getBranchId()) &&
                        "product-2".equals(r.getProductId()) &&
                        "Product 2".equals(r.getProductName()) &&
                        r.getStock() == 25
                    );
                    
                    return foundBranch1Product && foundBranch2Product;
                })
                .verifyComplete();
    }

    @Test
    void execute_WhenFranchiseNotFound_ShouldThrowException() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // When
        Mono<List<TopProductResponse>> result = getTopStockProductsByBranchUseCase.execute(franchiseId);

        // Then
        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    void execute_WhenNoBranchesHaveProducts_ShouldReturnEmptyList() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(productStockService.findTopStockPerBranch(franchise))
                .thenReturn(new HashMap<>());

        // When
        Mono<List<TopProductResponse>> result = getTopStockProductsByBranchUseCase.execute(franchiseId);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(List::isEmpty)
                .verifyComplete();
    }

    @Test
    void execute_WhenRepositoryFails_ShouldPropagateError() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<List<TopProductResponse>> result = getTopStockProductsByBranchUseCase.execute(franchiseId);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
