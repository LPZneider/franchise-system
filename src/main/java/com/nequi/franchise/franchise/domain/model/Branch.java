package com.nequi.franchise.franchise.domain.model;

import com.nequi.franchise.franchise.domain.model.valueobject.Name;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
public class Branch {
    @Getter
    private final String id;
    @Getter
    private Name name;
    private final List<Product> products;

    public Branch(String id, Name name) {
        this.id = id;
        this.name = name;
        this.products = new ArrayList<>();
    }

    public void rename(Name newName) {
        this.name = newName;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(String productId) {
        products.removeIf(p -> p.getId().equals(productId));
    }

    public Optional<Product> findProductById(String productId) {
        return products.stream().filter(p -> p.getId().equals(productId)).findFirst();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

}
