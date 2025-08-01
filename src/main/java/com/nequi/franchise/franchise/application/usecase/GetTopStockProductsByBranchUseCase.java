package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.domain.service.ProductStockService;
import com.nequi.franchise.franchise.entrypoint.rest.dto.TopProductResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class GetTopStockProductsByBranchUseCase {
    private final FranchiseRepository franchiseRepository;
    private final ProductStockService productStockService;

    public GetTopStockProductsByBranchUseCase(FranchiseRepository franchiseRepository,
                                              ProductStockService productStockService) {
        this.franchiseRepository = franchiseRepository;
        this.productStockService = productStockService;
    }

    public Mono<List<TopProductResponse>> execute(String franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .map(productStockService::findTopStockPerBranch)
                .map(map -> map.entrySet().stream()
                        .map(entry -> new TopProductResponse(
                                entry.getKey().getId(),
                                entry.getValue().getId(),
                                entry.getValue().getName().getValue(),
                                entry.getValue().getStock().getQuantity()
                        ))
                        .toList()
                );
    }
}
