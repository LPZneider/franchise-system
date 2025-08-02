package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.infrastructure.mapper.FranchiseResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GetAllFranchisesUseCase {
    private final FranchiseRepository franchiseRepository;

    public GetAllFranchisesUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Flux<FranchiseResponse> execute() {
        return franchiseRepository.findAll()
                .map(FranchiseResponseMapper::toResponse);
    }
}
