package com.nequi.franchise.franchise.entrypoint.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateFranchiseRequest {

    @NotBlank(message = "Name is required")
    private String name;

    public CreateFranchiseRequest() {}

    public CreateFranchiseRequest(String name) {
        this.name = name;
    }

}
