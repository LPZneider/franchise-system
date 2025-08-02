package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.ProductEntity;

public interface ProductMapper {
    static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return new Product(
            entity.getId(),
            new Name(entity.getName()),
            new Stock(entity.getStock())
        );
    }

    static ProductEntity toEntity(Product product) {
        if (product == null) return null;
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName().getValue());
        entity.setStock(product.getStock().getQuantity());
        return entity;
    }
}