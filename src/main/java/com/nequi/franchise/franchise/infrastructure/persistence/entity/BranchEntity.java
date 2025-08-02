package com.nequi.franchise.franchise.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchEntity {

    private String id;
    private String name;
    private List<ProductEntity> products = new ArrayList<>();
}
