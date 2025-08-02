package com.nequi.franchise.franchise.entrypoint.rest.dto;

import lombok.Data;

@Data
public class UpdateStockRequest {
    private int stock;

    public UpdateStockRequest() {}

    public UpdateStockRequest(int stock) {
        this.stock = stock;
    }
}
