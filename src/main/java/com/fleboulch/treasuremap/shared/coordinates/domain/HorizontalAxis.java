package com.fleboulch.treasuremap.shared.coordinates.domain;

public class HorizontalAxis extends Axis {

    public HorizontalAxis(int index) {
        super(index);
    }

    @Override
    public Axis increment() {
        int newIndex = index +1;
        return new HorizontalAxis(newIndex);
    }

    @Override
    public Axis decrement() {
        int newIndex = index - 1;
        return new HorizontalAxis(newIndex);
    }

}
