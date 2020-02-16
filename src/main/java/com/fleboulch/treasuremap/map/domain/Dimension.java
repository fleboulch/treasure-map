package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.Objects;

public class Dimension {

    private final int width;
    private final int height;

    public Dimension(int width, int height) {
        this.width = Domain.validatePositive(width, "The width should be positive");
        this.height = Domain.validatePositive(height, "The height should be positive");
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimension dimension = (Dimension) o;
        return width == dimension.width &&
                height == dimension.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", width, height);
    }
}
