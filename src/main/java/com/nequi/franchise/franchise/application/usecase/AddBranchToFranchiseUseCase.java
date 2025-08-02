package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.infrastructure.mapper.BranchResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.FRANCHISE_NOT_FOUND;

@Service
public class AddBranchToFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final FranchiseFactory franchiseFactory;

    public AddBranchToFranchiseUseCase(FranchiseRepository franchiseRepository,
                                       FranchiseFactory franchiseFactory) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseFactory = franchiseFactory;
    }

    public Mono<BranchResponse> execute(String franchiseId, CreateBranchRequest request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND.getMessage())))
                .flatMap(franchise -> {
                    Branch newBranch = franchiseFactory.createBranch(
                            UUID.randomUUID().toString(),
                            request.getName()
                    );
                    franchise.addBranch(newBranch);
                    return franchiseRepository.save(franchise)
                            .thenReturn(newBranch);
                })
                .map(branch -> BranchResponseMapper.toResponse(branch, franchiseId));
    }
}
