package com.nequi.franchise.franchise.domain.repository;

import com.nequi.franchise.franchise.domain.model.Franchise;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseRepository {
    Mono<Franchise> findById(String id);
    Flux<Franchise> findAll();
    Mono<Franchise> save(Franchise franchise);
    Mono<Void> deleteById(String id);
}
