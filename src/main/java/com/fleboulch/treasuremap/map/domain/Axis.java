package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public abstract class Axis {

    private final int index;

    protected Axis(int index) {
        this.index = Domain.validatePositiveOrZero(index, "The index for an axis should be positive");
    }

    public int index() {
        return index;
    }
}
