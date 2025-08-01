package com.nequi.franchise.franchise.infrastructure.persistence.repository;

import com.nequi.franchise.franchise.infrastructure.persistence.entity.FranchiseEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranchiseReactiveMongoRepository extends ReactiveMongoRepository<FranchiseEntity, String> {
}
