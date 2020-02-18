package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.List;
import java.util.Objects;

public class Explorer {

    // Add an UUID
    private final Name name;
    private final Position position;
    private final Movements movements;
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

    public MovementType nextMovement() {
        if (movements.movementTypes().isEmpty()) {
            throw new ExplorerHasNoMoreMovementException(this);
        }
        return movements.movementTypes().get(0);
    }

    public boolean hasValidCoordinates(Dimension dimension) {
        return position.coordinates().hasValidCoordinates(dimension);
    }

    public Name name() {
        return name;
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

    public boolean hasCollectedANewTreasure(Explorer oldExplorer) {
        return nbCollectedTreasures == oldExplorer.nbCollectedTreasures + 1;
    }

    public int collectTreasure() {
        return nbCollectedTreasures++;
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

    public Explorer turn(MovementType direction) {
        Position newPosition = direction.turn(position);
        return buildExplorerAfterAction(newPosition);

    }

    private Explorer buildExplorerAfterAction(Position newPosition) {
        return new Explorer(name, newPosition, movements, nbCollectedTreasures);
    }

    public Explorer goForward() {
        Coordinates newCoordinates = checkNextPosition();

        return buildExplorerAfterAction(new Position(orientation(), newCoordinates));
    }

    public Explorer goForwardAndCollect() {
        Explorer explorerAfterAction = goForward();
        explorerAfterAction.collectTreasure();
        return explorerAfterAction;
    }

    public Coordinates checkNextPosition() {
        switch (position.orientation().orientationType()) {
            case N:
                return goForwardNorth();
            case E:
                return goForwardEast();
            case S:
                return goForwardSouth();
            case W:
                return goForwardWest();
            default:
                throw new IllegalArgumentException("Unknown orientation"); // replace with custom exception
        }
    }

    private Coordinates goForwardWest() {
        return position.coordinates().goForwardWest();
    }

    private Coordinates goForwardNorth() {
        return position.coordinates().goForwardNorth();
    }

    private Coordinates goForwardEast() {
        return position.coordinates().goForwardEast();
    }

    private Coordinates goForwardSouth() {
        return position.coordinates().goForwardSouth();
    }

    public Explorer popMovement() {
        Movements movementsAfterAction = movements().popMovement();
        return new Explorer(name, position, movementsAfterAction, nbCollectedTreasures);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Explorer explorer = (Explorer) o;
        return nbCollectedTreasures == explorer.nbCollectedTreasures &&
                name.equals(explorer.name) &&
                position.equals(explorer.position) &&
                movements.equals(explorer.movements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position, movements, nbCollectedTreasures);
    }
}
