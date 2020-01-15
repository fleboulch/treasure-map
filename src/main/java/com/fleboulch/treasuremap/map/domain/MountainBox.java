package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

public class MountainBox extends PlainsBox {

    public MountainBox(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public String toString() {
        return String.format("M - %s", coordinates());
    }
}
