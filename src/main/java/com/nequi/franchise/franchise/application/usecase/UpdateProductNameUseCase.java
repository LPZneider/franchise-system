package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UpdateProductNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateProductNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, String branchId, String productId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")))
                .map(franchise -> {
                    var branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));
                    var product = branch.findProductById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
                    product.rename(new Name(newName));
                    return franchise;
                })
                .flatMap(franchiseRepository::save)
                .map(f -> new FranchiseResponse(
                        f.getId(),
                        f.getName().getValue(),
                        f.getBranches().stream()
                                .filter(branch -> branch.getId().equals(branchId))
                                .findFirst()
                                .map(List::of)
                                .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"))
                ));
    }
}

