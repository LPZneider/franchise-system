package com.nequi.franchise.franchise.entrypoint.rest.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private int stock;
    private String branchId;

    public ProductResponse(String id, String name, int stock, String branchId) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.branchId = branchId;
    }

}
