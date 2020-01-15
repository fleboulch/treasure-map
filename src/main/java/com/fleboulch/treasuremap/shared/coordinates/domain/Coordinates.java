package com.fleboulch.treasuremap.shared.coordinates.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;

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
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
