package com.nequi.franchise.franchise.entrypoint.rest.dto;

import lombok.Data;

@Data
public class TopProductResponse {
    private String branchId;
    private String productId;
    private String productName;
    private int stock;

    public TopProductResponse() {}

    public TopProductResponse(String branchId, String productId, String productName, int stock) {
        this.branchId = branchId;
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }
}
