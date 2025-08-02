package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @Mock
    private FranchiseFactory franchiseFactory;

    @InjectMocks
    private CreateFranchiseUseCase createFranchiseUseCase;

    private CreateFranchiseRequest request;
    private Franchise franchise;

    @BeforeEach
    void setUp() {
        request = new CreateFranchiseRequest();
        request.setName("Test Franchise");
        
        franchise = new Franchise("franchise-1", new Name("Test Franchise"));
    }

    @Test
    void execute_ShouldCreateFranchiseSuccessfully() {
        // Given
        when(franchiseFactory.createFranchise(anyString(), anyString()))
                .thenReturn(franchise);
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        // When
        Mono<FranchiseResponse> result = createFranchiseUseCase.execute(request);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    "franchise-1".equals(response.getId()) &&
                    "Test Franchise".equals(response.getName())
                )
                .verifyComplete();
    }

    @Test
    void execute_WhenRepositoryFails_ShouldPropagateError() {
        // Given
        when(franchiseFactory.createFranchise(anyString(), anyString()))
                .thenReturn(franchise);
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<FranchiseResponse> result = createFranchiseUseCase.execute(request);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
