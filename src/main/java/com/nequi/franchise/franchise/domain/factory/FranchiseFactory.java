package com.nequi.franchise.franchise.domain.factory;

import com.nequi.franchise.franchise.domain.model.Branch;
import com.nequi.franchise.franchise.domain.model.Franchise;
import com.nequi.franchise.franchise.domain.model.Product;
import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import org.springframework.stereotype.Component;

@Component
public class FranchiseFactory {
    public Franchise createFranchise(String id, String name) {
        return new Franchise(id, new Name(name));
    }

    public Branch createBranch(String id, String name) {
        return new Branch(id, new Name(name));
    }

    public Product createProduct(String id, String name, int stock) {
        return new Product(id, new Name(name), new Stock(stock));
    }
}
