package com.fleboulch.treasuremap.explorer.domain;

import java.util.List;

public class Movements {

    private final List<MovementType> movementTypes;

    public Movements(List<MovementType> movementTypes) {
        this.movementTypes = movementTypes;
    }

    public List<MovementType> movementTypes() {
        return movementTypes;
    }
}
