package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExplorerResolver implements ParameterResolver {

    // default values
    private static final Name DEFAULT_NAME = new Name("Laura");
    private static final OrientationType DEFAULT_ORIENTATION = OrientationType.E;
    private static final Coordinates DEFAULT_ONE_TWO_COORDINATES = Coordinates.of(1, 2);

    // coordinates
    private static final Coordinates ZERO_ZERO_COORDINATES = Coordinates.of(0, 0);
    private static final Coordinates ONE_ONE_COORDINATES = Coordinates.of(1, 1);
    private static final Coordinates ZERO_ONE_COORDINATES = Coordinates.of(0, 1);
    private static final Coordinates TWO_ONE_COORDINATES = Coordinates.of(2, 1);
    private static final Coordinates ONE_TWO_COORDINATES = Coordinates.of(1, 2);
    private static final Coordinates ONE_ZERO_COORDINATES = Coordinates.of(1, 0);

    // orientations
    private static final OrientationType SOUTH_ORIENTATION = OrientationType.S;
    private static final OrientationType NORTH_ORIENTATION = OrientationType.N;
    private static final OrientationType WEST_ORIENTATION = OrientationType.W;

    // movements
    private static final MovementType GO_FORWARD_MOVEMENT = MovementType.A;
    private static final MovementType TURN_RIGHT_MOVEMENT = MovementType.D;
    private static final MovementType TURN_LEFT_MOVEMENT = MovementType.G;
    private static final List<MovementType> EXAMPLE_SEQUENCE_MOVEMENT = List.of(
            GO_FORWARD_MOVEMENT,
            GO_FORWARD_MOVEMENT,
            TURN_RIGHT_MOVEMENT,
            GO_FORWARD_MOVEMENT,
            TURN_RIGHT_MOVEMENT,
            GO_FORWARD_MOVEMENT,
            TURN_LEFT_MOVEMENT,
            TURN_LEFT_MOVEMENT,
            GO_FORWARD_MOVEMENT
    );

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Explorer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        List<MovementType> movementType = buildMovementsType(parameterContext);
        Coordinates coordinates = buildCustomCoordinates(parameterContext);
        Name name = buildCustomName(parameterContext);
        OrientationType orientationType = buildOrientationType(parameterContext);
        int nbTreasures = buildNbTreasures(parameterContext);

        return buildExplorer(movementType, coordinates, name, orientationType, nbTreasures);
    }


    private int buildNbTreasures(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerWithOneTreasure.class)) {
            return 1;
        }
        return 0;
    }

    private OrientationType buildOrientationType(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerSouthOrientation.class)) {
            return SOUTH_ORIENTATION;
        }
        else if (parameterContext.isAnnotated(ExplorerNorthOrientation.class)) {
            return NORTH_ORIENTATION;
        }
        else if (parameterContext.isAnnotated(ExplorerWestOrientation.class)) {
            return WEST_ORIENTATION;
        }
        return DEFAULT_ORIENTATION;
    }

    private List<MovementType> buildMovementsType(ParameterContext parameterContext) {
        List<MovementType> movements = new ArrayList<>();
        if (parameterContext.isAnnotated(ExplorerWithOneGoForward.class)) {
            movements.add(GO_FORWARD_MOVEMENT);
        }
        if (parameterContext.isAnnotated(ExplorerTurnLeft.class)) {
            movements.add(TURN_LEFT_MOVEMENT);
        }
        if (parameterContext.isAnnotated(ExplorerTurnRight.class)) {
            movements.add(TURN_RIGHT_MOVEMENT);
        }
        if (parameterContext.isAnnotated(ExplorerWithExampleSequenceMovements.class)) {
            movements.addAll(EXAMPLE_SEQUENCE_MOVEMENT);
        }

        return movements;
    }


    private Name buildCustomName(ParameterContext parameterContext) {
        String name = DEFAULT_NAME.value();
        if (parameterContext.isAnnotated(ExplorerMichel.class)) {
            name = "Michel";
        }
        else if (parameterContext.isAnnotated(ExplorerAlberto.class)) {
            name = "Alberto";
        }
        return new Name(name);
    }

    private Coordinates buildCustomCoordinates(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerOneOneCoordinates.class)) {
            return ONE_ONE_COORDINATES;
        }
        else if (parameterContext.isAnnotated(ExplorerZeroZeroCoordinates.class)) {
            return ZERO_ZERO_COORDINATES;
        }
        else if (parameterContext.isAnnotated(ExplorerZeroOneCoordinates.class)) {
            return ZERO_ONE_COORDINATES;
        }
        else if (parameterContext.isAnnotated(ExplorerOneTwoCoordinates.class)) {
            return ONE_TWO_COORDINATES;
        }
        else if (parameterContext.isAnnotated(ExplorerOneZeroCoordinates.class)) {
            return ONE_ZERO_COORDINATES;
        }
        else if (parameterContext.isAnnotated(ExplorerTwoOneCoordinates.class)) {
            return TWO_ONE_COORDINATES;
        }

        return DEFAULT_ONE_TWO_COORDINATES;
    }

    private Explorer buildExplorer(List<MovementType> movementsType, Coordinates coordinates, Name name, OrientationType orientationType, int nbTreasures) {
        Explorer explorer = Explorer.of(
                name,
                coordinates,
                new Orientation(orientationType),
                buildRawMovements(movementsType)
        );

        IntStream.range(0,nbTreasures).forEach(index -> explorer.collectTreasure());

        return explorer;
    }


    private String buildRawMovements(List<MovementType> movementsType) {
        if (movementsType.isEmpty()) {
            return "";
        }
        return movementsType.stream()
                .map(MovementType::name)
                .collect(Collectors.joining());
    }

}
