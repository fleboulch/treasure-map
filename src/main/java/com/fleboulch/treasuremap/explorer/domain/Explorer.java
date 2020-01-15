package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.List;
import java.util.stream.Collectors;

public class Explorer {

    private final Name name;
    private final Coordinates coordinates;
    private final Orientation orientation;
    private final Movements movements;

    private Explorer(Name name, Coordinates coordinates, Orientation orientation, Movements movements) {
        this.name = Domain.validateNotNull(name, "Name should be not null");
        this.coordinates = Domain.validateNotNull(coordinates, "Coordinates should be not null");

        this.orientation = Domain.validateNotNull(orientation, "Orientation should be not null");
        this.movements = Domain.validateNotNull(movements, "Movements should be not null");
    }

    public static Explorer of(Name name, Coordinates coordinates, Orientation orientation, String rawMovements) {
        Domain.validateNotNull(rawMovements, "Raw movements should not be null");
        return new Explorer(name, coordinates, orientation, buildMovements(rawMovements));
    }

    private static Movements buildMovements(String rawMovements) {
        return new Movements(buildMovementTypeList(rawMovements));
    }

    private static List<MovementType> buildMovementTypeList(String rawMovements) {
        return rawMovements.chars()
                .mapToObj(explorerIndex -> toMovementType(explorerIndex, rawMovements))
                .collect(Collectors.toList());
    }

    private static MovementType toMovementType(int indexMovement, String rawMovements) {
        char movementAsChar = (char) indexMovement;
        return MovementType.valueOf(String.valueOf(movementAsChar));
    }

    public boolean hasValidCoordinates(Dimension dimension) {
        return coordinates.hasValidCoordinates(dimension);
    }

        public Name name() {
        return name;
    }

    public Coordinates coordinates() {
        return coordinates;
    }

    public Orientation orientation() {
        return orientation;
    }

    public Movements movements() {
        return movements;
    }

    @Override
    public String toString() {
        return "Explorer{" +
                "name=" + name +
                ", coordinates=" + coordinates +
                ", orientation=" + orientation +
                ", movements=" + movements +
                '}';
    }
}
