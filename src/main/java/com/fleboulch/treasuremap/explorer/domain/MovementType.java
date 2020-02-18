package com.fleboulch.treasuremap.explorer.domain;

import java.util.Objects;

public enum MovementType {
    A,
    G,
    D;

    public OrientationType turn(Orientation orientation) {
        if (Objects.equals(this, MovementType.D)) {
            switch (orientation.orientationType()) {
                case N:
                    return OrientationType.E;
                case E:
                    return OrientationType.S;
                case S:
                    return OrientationType.W;
                case W:
                    return OrientationType.N;
                default:
                    throw new IllegalArgumentException("Unknown orientation");

            }
        } else if (Objects.equals(this, MovementType.G)) {
            switch (orientation.orientationType()) {
                case N:
                    return OrientationType.W;
                case E:
                    return OrientationType.N;
                case S:
                    return OrientationType.E;
                case W:
                    return OrientationType.S;
                default:
                    throw new IllegalArgumentException("Unknown orientation");
            }
        }
        throw new IllegalArgumentException("Unknown direction for turn");
    }

}
