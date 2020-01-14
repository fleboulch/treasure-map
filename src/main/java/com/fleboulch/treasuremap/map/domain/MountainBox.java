package com.fleboulch.treasuremap.map.domain;

public class MountainBox extends PlainsBox {

    public MountainBox(HorizontalAxis x, VerticalAxis y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return String.format("M - %s - %s", x().index(), y().index());
    }
}
