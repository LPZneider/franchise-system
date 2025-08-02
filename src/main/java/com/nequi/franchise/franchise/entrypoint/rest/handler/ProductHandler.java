package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.GetTopStockProductsByBranchUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateProductNameUseCase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {
    private final GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;

    public ProductHandler(GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase,
                         UpdateProductNameUseCase updateProductNameUseCase) {
        this.getTopStockProductsByBranchUseCase = getTopStockProductsByBranchUseCase;
        this.updateProductNameUseCase = updateProductNameUseCase;
    }

    public Mono<ServerResponse> getTopStockProductsByBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return getTopStockProductsByBranchUseCase.execute(franchiseId)
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list));
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return request.bodyToMono(String.class)
                .flatMap(newName -> updateProductNameUseCase.execute(franchiseId, branchId, productId, newName))
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }
}
