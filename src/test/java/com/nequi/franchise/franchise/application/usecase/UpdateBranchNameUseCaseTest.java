package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBranchNameUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private UpdateBranchNameUseCase updateBranchNameUseCase;

    private String franchiseId;
    private String branchId;
    private String newName;
    private Franchise franchise;
    private Branch branch;

    @BeforeEach
    void setUp() {
        franchiseId = "franchise-1";
        branchId = "branch-1";
        newName = "Updated Branch Name";
        
        franchise = new Franchise("franchise-1", new Name("Test Franchise"));
        branch = new Branch("branch-1", new Name("Original Branch"));
        franchise.addBranch(branch);
    }

    @Test
    void execute_ShouldUpdateBranchNameSuccessfully() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        // When
        Mono<BranchResponse> result = updateBranchNameUseCase.execute(franchiseId, branchId, newName);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(response -> 
                    "branch-1".equals(response.getId()) &&
                    newName.equals(response.getName()) &&
                    franchiseId.equals(response.getFranchiseId())
                )
                .verifyComplete();
    }

    @Test
    void execute_WhenFranchiseNotFound_ShouldThrowException() {
        // Given
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // When
        Mono<BranchResponse> result = updateBranchNameUseCase.execute(franchiseId, branchId, newName);

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
        Mono<BranchResponse> result = updateBranchNameUseCase.execute(franchiseId, "non-existent-branch", newName);

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
        when(franchiseRepository.save(any(Franchise.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<BranchResponse> result = updateBranchNameUseCase.execute(franchiseId, branchId, newName);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
