package com.nequi.franchise.franchise.entrypoint.rest.handler;

import com.nequi.franchise.franchise.application.usecase.AddBranchToFranchiseUseCase;
import com.nequi.franchise.franchise.application.usecase.CreateFranchiseUseCase;
import com.nequi.franchise.franchise.application.usecase.UpdateFranchiseNameUseCase;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateBranchRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.CreateFranchiseRequest;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FranchiseHandler {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final FranchiseRepository franchiseRepository;
    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    public FranchiseHandler(CreateFranchiseUseCase createFranchiseUseCase,
                            FranchiseRepository franchiseRepository,
                            AddBranchToFranchiseUseCase addBranchToFranchiseUseCase,
                            UpdateFranchiseNameUseCase updateFranchiseNameUseCase) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.franchiseRepository = franchiseRepository;
        this.addBranchToFranchiseUseCase = addBranchToFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
    }

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(CreateFranchiseRequest.class)
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(franchiseResponse -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }

    public Mono<ServerResponse> getFranchise(ServerRequest request) {
        String id = request.pathVariable("id");
        return franchiseRepository.findById(id)
                .map(f -> new FranchiseResponse(f.getId(), f.getName().getValue()))
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> addBranch(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(CreateBranchRequest.class)
                .flatMap(body -> addBranchToFranchiseUseCase.execute(id, body))
                .flatMap(franchiseResponse -> ServerResponse.status(201)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }

    public Mono<ServerResponse> updateFranchiseName(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(String.class)
                .flatMap(newName -> updateFranchiseNameUseCase.execute(id, newName))
                .flatMap(franchiseResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(franchiseResponse));
    }
}
