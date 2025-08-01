package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateFranchiseNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateFranchiseNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")))
                .map(franchise -> {
                    franchise.rename(new Name(newName));
                    return franchise;
                })
                .flatMap(franchiseRepository::save)
                .map(f -> new FranchiseResponse(f.getId(), f.getName().getValue()));
    }
}
