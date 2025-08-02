package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.GetTopStockProductsByBranchUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateProductNameUseCase;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateNameRequest;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
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
                .flatMap(productsResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productsResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> updateProductName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return request.bodyToMono(UpdateNameRequest.class)
                .flatMap(body -> updateProductNameUseCase.execute(franchiseId, branchId, productId, body.getName()))
                .flatMap(productResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productResponse))
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        if (error instanceof FranchiseNotFoundException ||
            error instanceof BranchNotFoundException ||
            error instanceof ProductNotFoundException) {
            return ServerResponse.notFound().build();
        }
        return ServerResponse.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("Internal server error");
    }
}
