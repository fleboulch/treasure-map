package com.fleboulch.treasuremap.explorer.domain;

public class Orientation {

    private final OrientationType orientationType;

    public Orientation(OrientationType orientationType) {
        this.orientationType = orientationType;
    }

    public OrientationType orientationType() {
        return orientationType;
    }
}
