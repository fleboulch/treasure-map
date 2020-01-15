package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.map.domain.HorizontalAxis;
import com.fleboulch.treasuremap.map.domain.VerticalAxis;

import java.util.List;
import java.util.stream.Collectors;

public class Explorer {

    private final Name name;
    private final HorizontalAxis x;
    private final VerticalAxis y;
    private final Orientation orientation;
    private final Movements movements;

    private Explorer(Name name, HorizontalAxis x, VerticalAxis y, Orientation orientation, Movements movements) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.movements = movements;
    }

    public static Explorer of(Name name, HorizontalAxis x, VerticalAxis y, Orientation orientation, String rawMovements) {
        return new Explorer(name, x, y, orientation, buildMovements(rawMovements));
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
        char letter = (char) indexMovement;
        return MovementType.valueOf(String.valueOf(letter));
    }

    public Name name() {
        return name;
    }

    public HorizontalAxis x() {
        return x;
    }

    public VerticalAxis y() {
        return y;
    }

    public Orientation orientation() {
        return orientation;
    }

    public Movements movements() {
        return movements;
    }

}
