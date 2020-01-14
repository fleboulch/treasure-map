package com.fleboulch.treasuremap.map.domain;

public abstract class Box {

    private final HorizontalAxis x;
    private final VerticalAxis y;

    protected Box(HorizontalAxis x, VerticalAxis y) {
        this.x = x;
        this.y = y;
    }

    public HorizontalAxis x() {
        return x;
    }

    public VerticalAxis y() {
        return y;
    }
}
