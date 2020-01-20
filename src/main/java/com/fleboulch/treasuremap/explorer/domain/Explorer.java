package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Explorer {

    // Add an UUID

    private final Name name;
    private final Coordinates coordinates;
    private final Orientation orientation;
    private final Movements movements;
    private int nbCollectedTreasures;

    private Explorer(Name name, Coordinates coordinates, Orientation orientation, Movements movements, int nbCollectedTreasures) {
        this.name = Domain.validateNotNull(name, "Name should be not null");
        this.coordinates = Domain.validateNotNull(coordinates, "Coordinates should be not null");

        this.orientation = Domain.validateNotNull(orientation, "Orientation should be not null");
        this.movements = Domain.validateNotNull(movements, "Movements should be not null");
        this.nbCollectedTreasures = nbCollectedTreasures;
    }

    public static Explorer of(Name name, Coordinates coordinates, Orientation orientation, String rawMovements) {
        Domain.validateNotNull(rawMovements, "Raw movements should not be null");
        return new Explorer(name, coordinates, orientation, buildMovements(rawMovements), 0);
    }

    public boolean isOnMountain(TreasureMap treasureMap) {
        PlainsBox plainsBox = currentBox(treasureMap);

        if (plainsBox.isMountainType()) {
            throw new InvalidCurrentPositionException(name.value(), coordinates);
        } else {
            return false;
        }
    }

    public boolean isOnTreasure(TreasureMap treasureMap) {
        PlainsBox plainsBox = currentBox(treasureMap);

        return (plainsBox instanceof TreasureBox);
    }

    public boolean isOnPlains(TreasureMap treasureMap) {
        PlainsBox plainsBox = currentBox(treasureMap);

        return !(plainsBox instanceof TreasureBox) && !(plainsBox instanceof MountainBox);
    }

    private PlainsBox currentBox(TreasureMap treasureMap) {
        return treasureMap.from(coordinates);
    }

    private static Movements buildMovements(String rawMovements) {
        return new Movements(buildMovementTypeList(rawMovements));
    }

    private static List<MovementType> buildMovementTypeList(String rawMovements) {
        return rawMovements.chars()
                .mapToObj(explorerIndex -> toMovementType(explorerIndex, rawMovements))
                .collect(Collectors.toList());
    }

    public MovementType nextMovement() {
        if (movements.movementTypes().isEmpty()) {
            throw new ExplorerHasNoMoreMovementException(this);
        }
        return movements.movementTypes().get(0);
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
                ", coordinates=" + coordinates +
                ", orientation=" + orientation +
                ", movements=" + movements +
                ", nbCollectedTreasures=" + nbCollectedTreasures +
                '}';
    }

    public Explorer turn(MovementType direction) {
        if (Objects.equals(direction, MovementType.D)) {
            switch (orientation.orientationType()) {
                case N:
                    return buildExplorerAfterAction(OrientationType.E, null);
                case E:
                    return buildExplorerAfterAction(OrientationType.S, null);
                case S:
                    return buildExplorerAfterAction(OrientationType.W, null);
                case W:
                    return buildExplorerAfterAction(OrientationType.N, null);
                default: throw new IllegalArgumentException("Unknown orientation");

            }
        } else if (Objects.equals(direction, MovementType.G)) {
            switch (orientation.orientationType()) {
                case N:
                    return buildExplorerAfterAction(OrientationType.W, null);
                case E:
                    return buildExplorerAfterAction(OrientationType.N, null);
                case S:
                    return buildExplorerAfterAction(OrientationType.E, null);
                case W:
                    return buildExplorerAfterAction(OrientationType.S, null);
                default: throw new IllegalArgumentException("Unknown orientation");
            }
        }
        throw new IllegalArgumentException("Unknown direction for turn");
    }

    private Explorer buildExplorerAfterAction(OrientationType newOrientationType, Coordinates newCoordinates) {
        Orientation updatedOrientation = orientation();
        Coordinates updatedCoordinates = coordinates();

        if (!Objects.isNull(newOrientationType)) {
            updatedOrientation = new Orientation(newOrientationType);
        }
        if (!Objects.isNull(newCoordinates)) {
            updatedCoordinates = newCoordinates;
        }

        return new Explorer(name, updatedCoordinates, updatedOrientation, movements, nbCollectedTreasures);
    }

    public Explorer goForward() {
        Coordinates newCoordinates = checkNextPosition();

        return buildExplorerAfterAction(null, newCoordinates);
    }

    public Explorer goForwardAndCollect() {
        Explorer explorerAfterAction = goForward();
        explorerAfterAction.collectTreasure();
        return explorerAfterAction;
    }

    public Coordinates checkNextPosition() {
        switch (orientation.orientationType()) {
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
        return coordinates.goForwardWest();
    }

    private Coordinates goForwardNorth() {
        return coordinates.goForwardNorth();
    }

    private Coordinates goForwardEast() {
        return coordinates.goForwardEast();
    }

    private Coordinates goForwardSouth() {
        return coordinates.goForwardSouth();
    }

    public Explorer popMovement() {
        Movements movementsAfterAction = movements().popMovement();
        return new Explorer(name, coordinates, orientation, movementsAfterAction, nbCollectedTreasures);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Explorer explorer = (Explorer) o;
        return nbCollectedTreasures == explorer.nbCollectedTreasures &&
                name.equals(explorer.name) &&
                coordinates.equals(explorer.coordinates) &&
                orientation.equals(explorer.orientation) &&
                movements.equals(explorer.movements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, orientation, movements, nbCollectedTreasures);
    }
}
