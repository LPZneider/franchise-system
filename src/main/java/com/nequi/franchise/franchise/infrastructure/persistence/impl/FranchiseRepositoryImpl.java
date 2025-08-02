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

    public FranchiseRepositoryImpl(FranchiseReactiveMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongoRepository.findById(id)
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return mongoRepository.findAll()
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = FranchiseMapper.toEntity(franchise);
        return mongoRepository.save(entity)
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongoRepository.deleteById(id);
    }
}