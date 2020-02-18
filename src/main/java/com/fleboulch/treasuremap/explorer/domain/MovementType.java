package com.fleboulch.treasuremap.explorer.domain;

import java.util.Objects;

public enum MovementType {
    A,
    G,
    D;

    public Position executeMovement(Position position) {
        if (Objects.equals(this, MovementType.D)) {
            return new Position(position.orientation().rightTurn(), position.coordinates());
        } else if (Objects.equals(this, MovementType.G)) {
            return new Position(position.orientation().leftTurn(), position.coordinates());
        } else if (Objects.equals(this, MovementType.A)) {
            return position.goForward();
        } else {
            throw new IllegalArgumentException("Unknown movement type");

        }
    }

}
