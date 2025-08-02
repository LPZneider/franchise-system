package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class AddProductToBranchUseCase {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseFactory franchiseFactory;

    public AddProductToBranchUseCase(FranchiseRepository franchiseRepository,
                                     FranchiseFactory franchiseFactory) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseFactory = franchiseFactory;
    }

    public Mono<FranchiseResponse> execute(String franchiseId, String branchId, CreateProductRequest request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException("Franquicia no encontrada")))
                .map(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));
                    Product product = franchiseFactory.createProduct(UUID.randomUUID().toString(), request.getName(), request.getStock());
                    branch.addProduct(product);
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
