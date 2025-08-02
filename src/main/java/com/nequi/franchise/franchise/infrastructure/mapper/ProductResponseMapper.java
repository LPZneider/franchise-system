package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;

public class ProductResponseMapper {
    public static ProductResponse toResponse(Product product, String branchId) {
        return new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getStock().getQuantity(),
                branchId
        );
    }
}

