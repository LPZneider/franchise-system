package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.infrastructure.mapper.BranchResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.BRANCH_NOT_FOUND;
import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.FRANCHISE_NOT_FOUND;

@Service
public class UpdateBranchNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateBranchNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<BranchResponse> execute(String franchiseId, String branchId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND.getMessage())))
                .flatMap(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException(BRANCH_NOT_FOUND.getMessage()));

                    branch.rename(new Name(newName));

                    return franchiseRepository.save(franchise)
                            .thenReturn(branch);
                })
                .map(branch -> BranchResponseMapper.toResponse(branch, franchiseId));
    }
}
