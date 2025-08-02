package com.nequi.franchise.franchise.application.usecase;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.nequi.franchise.franchise.entrypoint.rest.exception.ExceptionMessage.*;


@Service
public class RemoveProductFromBranchUseCase {

    private final FranchiseRepository franchiseRepository;

    public RemoveProductFromBranchUseCase(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Void> execute(String franchiseId, String branchId, String productId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(FRANCHISE_NOT_FOUND.getMessage())))
                .flatMap(franchise -> {
                    Branch branch = franchise.findBranchById(branchId)
                            .orElseThrow(() -> new BranchNotFoundException(BRANCH_NOT_FOUND.getMessage()));

                    Product product = branch.findProductById(productId)
                            .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND.getMessage()));

                    branch.removeProduct(productId);

                    return franchiseRepository.save(franchise);
                })
                .then();
    }
}
