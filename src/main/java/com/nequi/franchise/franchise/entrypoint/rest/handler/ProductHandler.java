package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.GetTopStockProductsByBranchUseCase;
import com.nequi.franchise.franchise.entrypoint.rest.dto.TopProductResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ProductHandler {
    private final GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase;

    public ProductHandler(GetTopStockProductsByBranchUseCase getTopStockProductsByBranchUseCase) {
        this.getTopStockProductsByBranchUseCase = getTopStockProductsByBranchUseCase;
    }

    public Mono<ServerResponse> getTopStockProductsByBranch(ServerRequest request) {
        String franchiseId = request.pathVariable("franchiseId");
        return getTopStockProductsByBranchUseCase.execute(franchiseId)
                .flatMap(list -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(list));
    }
}
