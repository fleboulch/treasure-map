package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.Objects;

public class Position {

    private final Orientation orientation;
    private final Coordinates coordinates;

    public Position(Orientation orientation, Coordinates coordinates) {
        this.orientation = Domain.validateNotNull(orientation, "Orientation should be not null");
        this.coordinates = Domain.validateNotNull(coordinates, "Coordinates should be not null");
    }

    public Orientation orientation() {
        return orientation;
    }

    public Coordinates coordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return orientation.equals(position.orientation) &&
                coordinates.equals(position.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orientation, coordinates);
    }
}
