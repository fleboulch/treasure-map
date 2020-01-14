package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.Objects;

public class Dimension {

    private final Width width;
    private final Height height;

    public Dimension(Width width, Height height) {
        this.width = Domain.validateNotNull(width, "A dimension should have a width");
        this.height = Domain.validateNotNull(height, "A dimension should have a height");
    }

    public Width width() {
        return width;
    }

    public Height height() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return width.equals(dimension.width) &&
                height.equals(dimension.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", width.value(), height.value());
    }
}
