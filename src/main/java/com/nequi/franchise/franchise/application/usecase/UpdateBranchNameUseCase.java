package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateBranchNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateBranchNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, String branchId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")))
                .map(franchise -> {
                    var branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));
                    branch.rename(new Name(newName));
                    return franchise;
                })
                .flatMap(franchiseRepository::save)
                .map(f -> new FranchiseResponse(f.getId(), f.getName().getValue()));
    }
}

