package com.nequi.franchise.franchise.infrastructure.mapper;

import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.entrypoint.rest.dto.FranchiseResponse;
import java.util.stream.Collectors;

public class FranchiseResponseMapper {
    public static FranchiseResponse toResponse(Franchise franchise) {
        return new FranchiseResponse(
                franchise.getId(),
                franchise.getName().getValue(),
                franchise.getBranches().stream()
                        .map(branch -> BranchResponseMapper.toResponse(branch, franchise.getId()))
                        .collect(Collectors.toList())
        );
    }
}
