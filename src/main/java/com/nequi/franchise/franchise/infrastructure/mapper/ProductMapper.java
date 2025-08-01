package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toDomain(ProductEntity entity);

    ProductEntity toEntity(Product product);
}