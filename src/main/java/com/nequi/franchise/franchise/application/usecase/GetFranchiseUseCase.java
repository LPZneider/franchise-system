package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.infrastructure.mapper.FranchiseResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.FRANCHISE_NOT_FOUND;

@Service
public class GetFranchiseUseCase {
    private final FranchiseRepository franchiseRepository;

    public GetFranchiseUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND.getMessage())))
                .map(FranchiseResponseMapper::toResponse);
    }
}
