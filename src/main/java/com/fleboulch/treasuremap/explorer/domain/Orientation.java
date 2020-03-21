package com.fleboulch.treasuremap.explorer.domain;

import java.util.Objects;

public class Orientation {

    private final OrientationType orientationType;

    public Orientation(OrientationType orientationType) {
        this.orientationType = orientationType;
    }


    public Orientation leftTurn() {
        return new Orientation(orientationType.turnLeft());
    }

    public Orientation rightTurn() {
        return new Orientation(orientationType.turnRight());

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
