package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.AddBranchToFranchiseUseCase;
import com.nequi.franchise.franchise.application.usecase.CreateFranchiseUseCase;
import com.nequi.franchise.franchise.application.usecase.GetFranchiseUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateFranchiseNameUseCase;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.UpdateNameRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final GetFranchiseUseCase getFranchiseUseCase;
    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    public FranchiseHandler(CreateFranchiseUseCase createFranchiseUseCase,
                            GetFranchiseUseCase getFranchiseUseCase,
                            AddBranchToFranchiseUseCase addBranchToFranchiseUseCase,
                            UpdateFranchiseNameUseCase updateFranchiseNameUseCase) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.getFranchiseUseCase = getFranchiseUseCase;
        this.addBranchToFranchiseUseCase = addBranchToFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
    }

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(CreateFranchiseRequest.class)
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(franchiseResponse -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> getFranchise(ServerRequest request) {
        String id = request.pathVariable("id");
        return getFranchiseUseCase.execute(id)
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(CreateBranchRequest.class)
                .flatMap(body -> addBranchToFranchiseUseCase.execute(id, body))
                .flatMap(branchResponse -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(branchResponse))
                .onErrorResume(this::handleError);
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(UpdateNameRequest.class)
                .flatMap(body -> updateFranchiseNameUseCase.execute(id, body.getName()))
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse))
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> handleError(Throwable error) {
        if (error instanceof com.nequi.franchise.franchise.entrypoint.rest.exception.FranchiseNotFoundException) {
            return ServerResponse.notFound().build();
        }
        return ServerResponse.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("Internal server error");
    }
}
