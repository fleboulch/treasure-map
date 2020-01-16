package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.Objects;

public class Name {

    private final String value;

    public Name(String value) {
        this.value = Domain.validateNotNull(value, "value should not be null");
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "Name{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return value.equals(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
