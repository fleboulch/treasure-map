package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.List;

public class Movements {

    private final List<MovementType> movementTypes;

    public Movements(List<MovementType> movementTypes) {
        this.movementTypes = Domain.validateNotNull(movementTypes, "movement types should not be null");
    }

    public List<MovementType> movementTypes() {
        return movementTypes;
    }

    @Override
    public String toString() {
        return "Movements{" +
                "movementTypes=" + movementTypes +
                '}';
    }
}
