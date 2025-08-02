package com.nequi.franchise.franchise.entrypoint.rest.dto;

import com.nequi.franchise.franchise.domain.model.Branch;
import lombok.Data;

import java.util.List;

@Data
public class FranchiseResponse {

    private String id;
    private String name;
    private List<Branch> branches;

    public FranchiseResponse() {}

    public FranchiseResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public FranchiseResponse(String id, String name, List<Branch> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }
}
