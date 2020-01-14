package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public abstract class PlainsBox {

    private final HorizontalAxis x;
    private final VerticalAxis y;

    protected PlainsBox(HorizontalAxis x, VerticalAxis y) {
        this.x = Domain.validateNotNull(x, "Horizontal axis should not be null");
        this.y = Domain.validateNotNull(y, "Vertical axis should not be null");
    }

    public boolean isInside(Dimension dimension) {
        return x.index() < dimension.width().value() &&
                y.index() < dimension.height().value();
    }

    public HorizontalAxis x() {
        return x;
    }

    public VerticalAxis y() {
        return y;
    }
}