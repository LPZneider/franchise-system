package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllFranchisesUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private GetAllFranchisesUseCase getAllFranchisesUseCase;

    private Franchise franchise1;
    private Franchise franchise2;

    @BeforeEach
    void setUp() {
        franchise1 = new Franchise("franchise-1", new Name("Franchise 1"));
        franchise2 = new Franchise("franchise-2", new Name("Franchise 2"));
    }

    @Test
    void execute_ShouldReturnAllFranchises() {
        // Given
        when(franchiseRepository.findAll())
                .thenReturn(Flux.just(franchise1, franchise2));

        // When
        Flux<FranchiseResponse> result = getAllFranchisesUseCase.execute();

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    "franchise-1".equals(response.getId()) &&
                    "Franchise 1".equals(response.getName())
                )
                .expectNextMatches(response -> 
                    "franchise-2".equals(response.getId()) &&
                    "Franchise 2".equals(response.getName())
                )
                .verifyComplete();
    }

    @Test
    void execute_WhenNoFranchises_ShouldReturnEmpty() {
        // Given
        when(franchiseRepository.findAll())
                .thenReturn(Flux.empty());

        // When
        Flux<FranchiseResponse> result = getAllFranchisesUseCase.execute();

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void execute_WhenRepositoryFails_ShouldPropagateError() {
        // Given
        when(franchiseRepository.findAll())
                .thenReturn(Flux.error(new RuntimeException("Database error")));

        // When
        Flux<FranchiseResponse> result = getAllFranchisesUseCase.execute();

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
