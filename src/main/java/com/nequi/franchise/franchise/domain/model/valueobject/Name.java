package com.nequi.franchise.franchise.domain.model.valueobject;

import java.util.Objects;

public class Name {
    private final String value;

    public Name(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Name && value.equals(((Name) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
