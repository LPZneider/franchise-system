package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.factory.FranchiseFactory;
import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.infrastructure.mapper.ProductResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.*;

@Service
public class AddProductToBranchUseCase {

    private final FranchiseRepository franchiseRepository;
    private final FranchiseFactory franchiseFactory;

    public AddProductToBranchUseCase(FranchiseRepository franchiseRepository,
                                     FranchiseFactory franchiseFactory) {
        this.franchiseRepository = franchiseRepository;
        this.franchiseFactory = franchiseFactory;
    }

    public Mono<ProductResponse> execute(String franchiseId, String branchId, CreateProductRequest request) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND.getMessage())))
                .flatMap(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException(BRANCH_NOT_FOUND.getMessage()));

                    Product product = franchiseFactory.createProduct(
                            UUID.randomUUID().toString(),
                            request.getName(),
                            request.getStock()
                    );
                    branch.addProduct(product);
                    return franchiseRepository.save(franchise)
                            .thenReturn(product);
                })
                .map(product -> ProductResponseMapper.toResponse(product, branchId));
    }
}
