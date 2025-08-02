package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AddBranchToFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final FranchiseFactory franchiseFactory;

    public AddBranchToFranchiseUseCase(FranchiseRepository franchiseRepository,
                                       FranchiseFactory franchiseFactory) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseFactory = franchiseFactory;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, CreateBranchRequest request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")))
                .map(franchise -> {
                    Branch newBranch = franchiseFactory.createBranch(UUID.randomUUID().toString(), request.getName());
                    franchise.addBranch(newBranch);
                    return franchise;
                })
                .flatMap(franchiseRepository::save)
                .map(franquicia -> new FranchiseResponse(franquicia.getId(), franquicia.getName().getValue(), franquicia.getBranches()));

    }
}
