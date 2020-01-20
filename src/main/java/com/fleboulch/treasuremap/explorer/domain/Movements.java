package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Movements {

    private final List<MovementType> movementTypes;

    public Movements(List<MovementType> movementTypes) {
        this.movementTypes = Domain.validateNotNull(movementTypes, "movement types should not be null");
    }

    public List<MovementType> movementTypes() {
        return movementTypes;
    }

    public Movements popMovement() {
        if (movementTypes.isEmpty()) {
            return new Movements(movementTypes);
        }
        List<MovementType> list = new ArrayList<>(movementTypes);
        list.remove(0);
        return new Movements(list);
    }

    @Override
    public String toString() {
        return "Movements{" +
                "movementTypes=" + movementTypes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movements movements = (Movements) o;
        return movementTypes.equals(movements.movementTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementTypes);
    }
}
