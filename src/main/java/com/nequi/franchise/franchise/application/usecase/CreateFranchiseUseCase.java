package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CreateFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;
    private final FranchiseFactory franchiseFactory;

    public CreateFranchiseUseCase(FranchiseRepository franchiseRepository,
                                  FranchiseFactory franchiseFactory) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseFactory = franchiseFactory;
    }

    public Mono<FranchiseResponse> execute(CreateFranchiseRequest request) {
        Franchise franchise = franchiseFactory.createFranchise(UUID.randomUUID().toString(), request.getName());
        return franchiseRepository.save(franchise)
                .map(franquicia -> new FranchiseResponse(franquicia.getId(), franquicia.getName().getValue()));
    }
}
