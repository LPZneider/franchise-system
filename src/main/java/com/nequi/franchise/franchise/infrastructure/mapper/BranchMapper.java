package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.BranchEntity;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;

public interface BranchMapper {
    static Branch toDomain(BranchEntity entity) {
        if (entity == null) return null;
        Branch branch = new Branch(
            entity.getId(),
            new Name(entity.getName())
        );
        if (entity.getProducts() != null) {
            for (ProductEntity pe : entity.getProducts()) {
                branch.addProduct(ProductMapper.toDomain(pe));
            }
        }
        return branch;
    }

    static BranchEntity toEntity(Branch branch) {
        if (branch == null) return null;
        BranchEntity entity = new BranchEntity();
        entity.setId(branch.getId());
        entity.setName(branch.getName().getValue());
        List<ProductEntity> products = branch.getProducts().stream()
            .map(ProductMapper::toEntity)
            .collect(Collectors.toList());
        entity.setProducts(products);
        return entity;
    }
}
