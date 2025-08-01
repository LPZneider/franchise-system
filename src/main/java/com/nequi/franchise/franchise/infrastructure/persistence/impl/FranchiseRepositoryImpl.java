package com.nequi.franchise.franchise.infrastructure.persistence.impl;

import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.repository.FranchiseRepository;
import com.nequi.franchise.franchise.infrastructure.mapper.FranchiseMapper;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.FranchiseEntity;
import com.nequi.franchise.franchise.infrastructure.persistence.repository.FranchiseReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseRepositoryImpl implements FranchiseRepository {

    private final FranchiseReactiveMongoRepository mongoRepository;
    private final FranchiseMapper mapper;

    public FranchiseRepositoryImpl(FranchiseReactiveMongoRepository mongoRepository,
                                   FranchiseMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongoRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return mongoRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = mapper.toEntity(franchise);
        return mongoRepository.save(entity)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongoRepository.deleteById(id);
    }
}