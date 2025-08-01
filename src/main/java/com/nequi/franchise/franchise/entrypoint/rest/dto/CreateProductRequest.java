package com.nequi.franchise.franchise.entrypoint.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be zero or greater")
    private Integer stock;

    public CreateProductRequest() {
    }

    public CreateProductRequest(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

}
