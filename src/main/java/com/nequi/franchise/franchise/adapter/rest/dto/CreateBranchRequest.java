package com.nequi.franchise.franchise.adapter.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBranchRequest {
    @NotBlank(message = "Branch name is required")
    private String name;

    public CreateBranchRequest() {
    }

    public CreateBranchRequest(String name) {
        this.name = name;
    }

}
