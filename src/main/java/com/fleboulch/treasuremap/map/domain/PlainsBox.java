package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.shared.coordinates.domain.HorizontalAxis;
import com.fleboulch.treasuremap.shared.coordinates.domain.VerticalAxis;

import java.util.Objects;

public abstract class PlainsBox {

    private final HorizontalAxis x;
    private final VerticalAxis y;

    protected PlainsBox(HorizontalAxis x, VerticalAxis y) {
        this.x = Domain.validateNotNull(x, "Horizontal axis should not be null");
        this.y = Domain.validateNotNull(y, "Vertical axis should not be null");
    }

    // duplicated code (create coordinates abstraction)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlainsBox plainsBox = (PlainsBox) o;
        return x.equals(plainsBox.x) &&
                y.equals(plainsBox.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
