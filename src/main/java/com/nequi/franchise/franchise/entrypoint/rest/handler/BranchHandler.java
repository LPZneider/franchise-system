package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.AddProductToBranchUseCase;
import com.nequi.franchise.franchise.application.usecase.RemoveProductFromBranchUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateBranchNameUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateProductStockUseCase;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateNameRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateStockRequest;
import com.nequi.franchise.franchise.entrypoint.rest.exception.BranchNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException;
import com.nequi.franchise.franchise.entrypoint.rest.exception.ProductNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BranchHandler {
    private final AddProductToBranchUseCase addProductToBranchUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final RemoveProductFromBranchUseCase removeProductFromBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    public BranchHandler(AddProductToBranchUseCase addProductToBranchUseCase,
                         UpdateProductStockUseCase updateProductStockUseCase,
                         RemoveProductFromBranchUseCase removeProductFromBranchUseCase,
                         UpdateBranchNameUseCase updateBranchNameUseCase) {
        this.addProductToBranchUseCase = addProductToBranchUseCase;
        this.updateProductStockUseCase = updateProductStockUseCase;
        this.removeProductFromBranchUseCase = removeProductFromBranchUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
    }

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");

        return request.bodyToMono(CreateProductRequest.class)
                .flatMap(body -> addProductToBranchUseCase.execute(franchiseId, branchId, body))
                .flatMap(productResponse -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return request.bodyToMono(UpdateStockRequest.class)
                .flatMap(body -> updateProductStockUseCase.execute(franchiseId, branchId, productId, body.getStock()))
                .flatMap(productResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(productResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");

        return removeProductFromBranchUseCase.execute(franchiseId, branchId, productId)
                .then(ServerResponse.noContent().build())
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");

        return request.bodyToMono(UpdateNameRequest.class)
                .flatMap(body -> updateBranchNameUseCase.execute(franchiseId, branchId, body.getName()))
                .flatMap(branchResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(branchResponse))
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
