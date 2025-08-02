package com.nequi.franchise.franchise.entrypoint.rest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FranchiseResponse {

    private String id;
    private String name;
    private List<BranchResponse> branches;

    public FranchiseResponse() {}

    public FranchiseResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public FranchiseResponse(String id, String name, List<BranchResponse> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }
}
