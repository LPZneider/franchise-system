package com.nequi.franchise.franchise.domain.model;

import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import com.nequi.franchise.franchise.domain.model.valueobject.Stock;
import lombok.ToString;

@ToString
public class Product {
    private final String id;
    private Name name;
    private Stock stock;

    public Product(String id, Name name, Stock stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public void rename(Name newName) {
        this.name = newName;
    }

    public void updateStock(Stock newStock) {
        this.stock = newStock;
    }

    public String getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Stock getStock() {
        return stock;
    }
}
