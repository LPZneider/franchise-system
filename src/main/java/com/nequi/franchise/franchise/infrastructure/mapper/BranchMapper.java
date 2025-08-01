package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.BranchEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface BranchMapper {

    Branch toDomain(BranchEntity entity);

    BranchEntity toEntity(Branch branch);
}
