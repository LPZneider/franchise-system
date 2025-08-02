package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.BranchEntity;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.FranchiseEntity;

import java.util.List;
import java.util.stream.Collectors;

public interface FranchiseMapper {
    static Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) return null;
        Franchise franchise = new Franchise(
            entity.getId(),
            new Name(entity.getName())
        );
        if (entity.getBranches() != null) {
            for (BranchEntity be : entity.getBranches()) {
                franchise.addBranch(BranchMapper.toDomain(be));
            }
        }
        return franchise;
    }

    static FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) return null;
        FranchiseEntity entity = new FranchiseEntity();
        entity.setId(franchise.getId());
        entity.setName(franchise.getName().getValue());
        List<BranchEntity> branches = franchise.getBranches().stream()
            .map(BranchMapper::toEntity)
            .collect(Collectors.toList());
        entity.setBranches(branches);
        return entity;
    }
}