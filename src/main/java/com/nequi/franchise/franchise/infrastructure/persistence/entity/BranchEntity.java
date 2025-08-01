package com.nequi.franchise.franchise.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
public class BranchEntity {

    private String id;
    private String name;
    private List<ProductEntity> products = new ArrayList<>();
}
