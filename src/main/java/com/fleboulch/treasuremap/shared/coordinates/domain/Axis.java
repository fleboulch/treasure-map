package com.fleboulch.treasuremap.shared.coordinates.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.Objects;

public class Axis {

    protected final int index;

    protected Axis(int index) {
        this.index = Domain.validatePositiveOrZero(index, "The index for an axis should be positive");
    }

    public Axis increment() {
        int newIndex = index + 1;
        return new Axis(newIndex);
    }

    public Axis decrement() {
        if (index == 0) {
            return this;
        }
        int newIndex = index - 1;
        return new Axis(newIndex);
    }

    public int index() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Axis axis = (Axis) o;
        return index == axis.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return "Axis{" +
                "index=" + index +
                '}';
    }

}
