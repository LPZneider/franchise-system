package com.nequi.franchise.franchise.entrypoint.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class BranchResponse {
    private String id;
    private String name;
    private List<ProductResponse> products;
    private String franchiseId;

    public BranchResponse(String id, String name, List<ProductResponse> products, String franchiseId) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.franchiseId = franchiseId;
    }
}

