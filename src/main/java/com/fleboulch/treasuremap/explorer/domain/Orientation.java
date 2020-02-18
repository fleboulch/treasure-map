package com.fleboulch.treasuremap.explorer.domain;

import java.util.Objects;

public class Orientation {

    private final OrientationType orientationType;

    public Orientation(OrientationType orientationType) {
        this.orientationType = orientationType;
    }


    public Orientation leftTurn() {
        switch (orientationType) {
            case N:
                return new Orientation(OrientationType.W);
            case E:
                return new Orientation(OrientationType.N);
            case S:
                return new Orientation(OrientationType.E);
            case W:
                return new Orientation(OrientationType.S);
            default:
                throw new IllegalArgumentException("Unknown orientation");
        }
    }

    public Orientation rightTurn() {
        switch (orientationType) {
            case N:
                return new Orientation(OrientationType.E);
            case E:
                return new Orientation(OrientationType.S);
            case S:
                return new Orientation(OrientationType.W);
            case W:
                return new Orientation(OrientationType.N);
            default:
                throw new IllegalArgumentException("Unknown orientation");

        }
    }

    public OrientationType orientationType() {
        return orientationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orientation that = (Orientation) o;
        return orientationType == that.orientationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientationType);
    }

    @Override
    public String toString() {
        return "Orientation{" +
                "orientationType=" + orientationType +
                '}';
    }

}
