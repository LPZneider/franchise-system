package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.AddProductToBranchUseCase;
import com.nequi.franchise.franchise.application.usecase.RemoveProductFromBranchUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateBranchNameUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateProductStockUseCase;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateProductRequest;
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
                .flatMap(franchiseResponse -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return request.bodyToMono(Integer.class)
                .flatMap(newStock -> updateProductStockUseCase.execute(franchiseId, branchId, productId, newStock))
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }

    public Mono<ServerResponse> removeProduct(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        String productId = request.pathVariable("productId");
        return removeProductFromBranchUseCase.execute(franchiseId, branchId, productId)
                .flatMap(franchiseResponse -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        String branchId = request.pathVariable("branchId");
        return request.bodyToMono(String.class)
                .flatMap(newName -> updateBranchNameUseCase.execute(franchiseId, branchId, newName))
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }
}
