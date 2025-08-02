package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
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
class UpdateFranchiseNameUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    private String franchiseId;
    private String newName;
    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchiseId = "franchise-1";
        newName = "Updated Franchise Name";
        franchise = new Franchise("franchise-1", new Name("Original Franchise"));
    }

    @Test
    void execute_ShouldUpdateFranchiseNameSuccessfully() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        // When
        Mono<FranchiseResponse> result = updateFranchiseNameUseCase.execute(franchiseId, newName);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    "franchise-1".equals(response.getId()) &&
                    newName.equals(response.getName())
                )
                .verifyComplete();
    }

    @Test
    void execute_WhenFranchiseNotFound_ShouldThrowException() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // When
        Mono<FranchiseResponse> result = updateFranchiseNameUseCase.execute(franchiseId, newName);

        // Then
        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
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
        Mono<FranchiseResponse> result = updateFranchiseNameUseCase.execute(franchiseId, newName);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
