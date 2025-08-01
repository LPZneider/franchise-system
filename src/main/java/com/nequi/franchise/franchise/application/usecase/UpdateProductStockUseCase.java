package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.adapter.rest.dto.FranchiseResponse;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
public class UpdateProductStockUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateProductStockUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, String branchId, String productId, int newStock) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .map(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new IllegalArgumentException("Branch not found"));
                    Product product = branch.findProductById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    product.updateStock(new Stock(newStock));
                    return franchise;
                })
                .flatMap(franchiseRepository::save)
                .map(f -> new FranchiseResponse(f.getId(), f.getName().getValue()));
    }
}
