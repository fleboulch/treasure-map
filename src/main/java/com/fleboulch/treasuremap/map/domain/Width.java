package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.Objects;

public class Width {

    private final int value;

    public Width(int value) {
        this.value = Domain.validatePositive(value, "A width should have a positive value");
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Width width = (Width) o;
        return value == width.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
