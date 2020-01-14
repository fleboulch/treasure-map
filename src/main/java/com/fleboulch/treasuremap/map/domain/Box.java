package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public abstract class Box {

    private final HorizontalAxis x;
    private final VerticalAxis y;

    protected Box(HorizontalAxis x, VerticalAxis y) {
        this.x = Domain.validateNotNull(x, "Horizontal axis should not be null");
        this.y = Domain.validateNotNull(y, "Vertical axis should not be null");
    }

    public HorizontalAxis x() {
        return x;
    }

    public VerticalAxis y() {
        return y;
    }
}
