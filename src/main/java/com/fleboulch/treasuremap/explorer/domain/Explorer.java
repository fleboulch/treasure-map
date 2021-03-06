package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.List;
import java.util.Objects;

public class Explorer {

    // Add an UUID
    private final Name name;
    private Position position;
    private Movements movements;
    private int nbCollectedTreasures;

    private Explorer(Name name, Position position, Movements movements, int nbCollectedTreasures) {
        this.name = Domain.validateNotNull(name, "Name should be not null");
        this.position = Domain.validateNotNull(position, "Position should be not null");
        this.movements = Domain.validateNotNull(movements, "Movements should be not null");
        this.nbCollectedTreasures = nbCollectedTreasures;
    }

    public static Explorer of(Name name, Position position, List<MovementType> movementTypes) {
        Domain.validateNotNull(movementTypes, "Raw movements should not be null");
        return new Explorer(name, position, new Movements(movementTypes), 0);
    }

    public int collectTreasure() {
        return nbCollectedTreasures++;
    }

    public boolean hasValidCoordinates(Dimension dimension) {
        return position.coordinates().hasValidCoordinates(dimension);
    }

    public MovementType nextMovement() {
        if (movements.movementTypes().isEmpty()) {
            throw new ExplorerHasNoMoreMovementException(this);
        }
        return movements.movementTypes().get(0);
    }

    public Coordinates checkNextPositionWhenGoForward() {
        return position.goForward().coordinates();
    }

    public Explorer popMovement() {
        return new Explorer(name, position, movements.popMovement(), nbCollectedTreasures);

    }

    public Explorer turn(MovementType direction) {
        if (Objects.equals(direction, MovementType.A)) {
            throw new InvalidMovementTypeForTurnException(direction);
        }
        Position newPosition = direction.executeMovement(this.position);
        return new Explorer(name, newPosition, movements, nbCollectedTreasures);

    }

    public Explorer goForward() {
        Position newPosition = MovementType.A.executeMovement(this.position);

        return new Explorer(name, newPosition, movements, nbCollectedTreasures);
    }

    public Explorer goForwardAndCollect() {
        Explorer newExplorer = goForward();
        newExplorer.collectTreasure();

        return newExplorer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Explorer explorer = (Explorer) o;
        return name.equals(explorer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Name name() {
        return name;
    }

    public Position position() {
        return position;
    }

    public Coordinates coordinates() {
        return position.coordinates();
    }

    public Orientation orientation() {
        return position.orientation();
    }

    public Movements movements() {
        return movements;
    }

    public int nbCollectedTreasures() {
        return nbCollectedTreasures;
    }

    @Override
    public String toString() {
        return "Explorer{" +
                "name=" + name +
                ", coordinates=" + position.coordinates() +
                ", orientation=" + position.orientation() +
                ", movements=" + movements +
                ", nbCollectedTreasures=" + nbCollectedTreasures +
                '}';
    }

}
