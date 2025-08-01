package com.nequi.franchise.franchise.domain.model.valueobject;

import java.util.Objects;

public class Stock {
    private final int quantity;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Stock increase(int amount) {
        return new Stock(this.quantity + amount);
    }

    public Stock decrease(int amount) {
        return new Stock(this.quantity - amount);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Stock && quantity == ((Stock) o).quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
