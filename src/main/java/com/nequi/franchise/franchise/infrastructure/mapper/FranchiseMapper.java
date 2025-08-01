package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.FranchiseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BranchMapper.class)
public interface FranchiseMapper {

    Franchise toDomain(FranchiseEntity entity);

    FranchiseEntity toEntity(Franchise franchise);
}
