package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.map.domain.HorizontalAxis;
import com.fleboulch.treasuremap.map.domain.VerticalAxis;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Explorer {

    private final Name name;
    private final HorizontalAxis x;
    private final VerticalAxis y;
    private final Orientation orientation;
    private final Movements movements;

    private Explorer(Name name, HorizontalAxis x, VerticalAxis y, Orientation orientation, Movements movements) {
        this.name = Domain.validateNotNull(name, "Name should be not null");
        this.x = Domain.validateNotNull(x, "Horizontal axis should be not null");
        this.y = Domain.validateNotNull(y, "Vertical axis should be not null");
        this.orientation = Domain.validateNotNull(orientation, "Orientation should be not null");
        this.movements = Domain.validateNotNull(movements, "Movements should be not null");;
    }

    public static Explorer of(Name name, HorizontalAxis x, VerticalAxis y, Orientation orientation, String rawMovements) {
        Domain.validateNotNull(rawMovements, "Raw movements should not be null");
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
