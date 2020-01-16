package com.fleboulch.treasuremap.shared.coordinates.domain;

public class VerticalAxis extends Axis {

    public VerticalAxis(int index) {
        super(index);
    }

    @Override
    public Axis increment() {
        int newIndex = index + 1;
        return new VerticalAxis(newIndex);
    }

    @Override
    public Axis decrement() {
        int newIndex = index - 1;
        return new VerticalAxis(newIndex);
    }

}
