package com.fleboulch.treasuremap.explorer.domain;

import java.util.Objects;

public enum MovementType {
    A,
    G,
    D;

    public Position turn(Position position) {
        if (Objects.equals(this, MovementType.D)) {
            switch (position.orientation().orientationType()) {
                case N:
                    return new Position(new Orientation(OrientationType.E), position.coordinates());
                case E:
                    return new Position(new Orientation(OrientationType.S), position.coordinates());
                case S:
                    return new Position(new Orientation(OrientationType.W), position.coordinates());
                case W:
                    return new Position(new Orientation(OrientationType.N), position.coordinates());
                default:
                    throw new IllegalArgumentException("Unknown position");

            }
        } else if (Objects.equals(this, MovementType.G)) {
            switch (position.orientation().orientationType()) {
                case N:
                    return new Position(new Orientation(OrientationType.W), position.coordinates());
                case E:
                    return new Position(new Orientation(OrientationType.N), position.coordinates());
                case S:
                    return new Position(new Orientation(OrientationType.E), position.coordinates());
                case W:
                    return new Position(new Orientation(OrientationType.S), position.coordinates());
                default:
                    throw new IllegalArgumentException("Unknown position");
            }
        }
        throw new IllegalArgumentException("Unknown direction for turn");
    }

}
