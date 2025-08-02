package com.nequi.franchise.franchise.entrypoint.rest.dto;

import lombok.Data;

@Data
public class UpdateNameRequest {
    private String name;

    public UpdateNameRequest() {}

    public UpdateNameRequest(String name) {
        this.name = name;
    }
}
