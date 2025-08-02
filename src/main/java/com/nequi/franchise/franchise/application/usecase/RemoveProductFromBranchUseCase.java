package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RemoveProductFromBranchUseCase {

    private final FranchiseRepository franchiseRepository;

    public RemoveProductFromBranchUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")))
                .map(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));
                    Product product = branch.findProductById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));
                    branch.removeProduct(productId);
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
