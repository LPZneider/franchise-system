package com.nequi.franchise.franchise.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "franchises")
public class FranchiseEntity {

    @Id
    private String id;

    private String name;

    private List<BranchEntity> branches = new ArrayList<>();
}
