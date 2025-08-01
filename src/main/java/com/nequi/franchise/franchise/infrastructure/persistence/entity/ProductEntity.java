package com.nequi.franchise.franchise.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class ProductEntity {

    private String id;
    private String name;
    private int stock;

}
