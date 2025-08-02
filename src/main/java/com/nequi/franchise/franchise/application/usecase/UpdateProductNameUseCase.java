package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
import com.nequi.franchise.franchise.infrastructure.mapper.ProductResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.*;

@Service
public class UpdateProductNameUseCase {
    private final FranchiseRepository franchiseRepository;

    public UpdateProductNameUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<ProductResponse> execute(String franchiseId, String branchId, String productId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND.getMessage())))
                .flatMap(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException(BRANCH_NOT_FOUND.getMessage()));

                    Product product = branch.findProductById(productId)
                            .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND.getMessage()));

                    product.rename(new Name(newName));

                    return franchiseRepository.save(franchise)
                            .thenReturn(product);
                })
                .map(product -> ProductResponseMapper.toResponse(product, branchId));
    }
}
