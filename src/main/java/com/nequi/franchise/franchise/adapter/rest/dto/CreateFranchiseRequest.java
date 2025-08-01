package com.nequi.franchise.franchise.adapter.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateFranchiseRequest {

    @NotBlank(message = "Name is required")
    private String name;

    public CreateFranchiseRequest() {}

    public CreateFranchiseRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
