package com.fleboulch.treasuremap.shared.coordinates.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;

import java.util.Objects;

public class Coordinates {
    private final HorizontalAxis x;
    private final VerticalAxis y;

    private Coordinates(HorizontalAxis x, VerticalAxis y) {
        this.x = Domain.validateNotNull(x, "Horizontal axis should be not null");
        this.y = Domain.validateNotNull(y, "Vertical axis should be not null");
    }

    public static Coordinates of(int x, int y) {
        return new Coordinates(new HorizontalAxis(x), new VerticalAxis(y));
    }

    public HorizontalAxis x() {
        return x;
    }

    public VerticalAxis y() {
        return y;
    }

    public boolean hasValidCoordinates(Dimension dimension) {
        return x.index() < dimension.width().value() &&
                y.index() < dimension.height().value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x.equals(that.x) &&
                y.equals(that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", x.index(), y.index());
    }
}