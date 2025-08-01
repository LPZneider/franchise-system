package com.nequi.franchise.franchise.adapter.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopProductResponse {

    private String branchId;
    private String productId;
    private String productName;
    private int stock;

    public TopProductResponse() {
    }

    public TopProductResponse(String branchId, String productId, String productName, int stock) {
        this.branchId = branchId;
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }

}
