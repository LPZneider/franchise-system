package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.entrypoint.rest.dto.BranchResponse;
import com.nequi.franchise.franchise.entrypoint.rest.dto.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;

public class BranchResponseMapper {
    public static BranchResponse toResponse(Branch branch, String franchiseId) {
        List<ProductResponse> products = branch.getProducts().stream()
                .map(product -> ProductResponseMapper.toResponse(product, branch.getId()))
                .collect(Collectors.toList());
        return new BranchResponse(
                branch.getId(),
                branch.getName().getValue(),
                products,
                franchiseId
        );
    }
}

