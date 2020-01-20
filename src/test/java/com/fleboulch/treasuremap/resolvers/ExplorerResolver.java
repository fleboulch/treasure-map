package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ExplorerResolver implements ParameterResolver {

    // default values
    private static final Name DEFAULT_NAME = new Name("Laura");
    public static final OrientationType DEFAULT_ORIENTATION = OrientationType.E;
    private static final Coordinates DEFAULT_ONE_TWO_COORDINATES = Coordinates.of(1, 2);
    public static final MovementType DEFAULT_EMPTY_MOVEMENT = null;

    // coordinates
    private static final Coordinates ZERO_ZERO_COORDINATES = Coordinates.of(0, 0);
    private static final Coordinates ONE_ONE_COORDINATES = Coordinates.of(1, 1);
    private static final Coordinates ZERO_ONE_COORDINATES = Coordinates.of(0, 1);

    // orientations
    public static final OrientationType SOUTH_ORIENTATION = OrientationType.S;

    // movements
    public static final MovementType GO_FORWARD_MOVEMENT = MovementType.A;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Explorer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        MovementType movementType = buildMovementType(parameterContext);
        Coordinates coordinates = buildCustomCoordinates(parameterContext);
        Name name = buildCustomName(parameterContext);
        OrientationType orientationType = buildOrientationType(parameterContext);

        return buildExplorer(movementType, coordinates, name, orientationType);
    }

    private OrientationType buildOrientationType(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerSouthOrientation.class)) {
            return SOUTH_ORIENTATION;
        }
        return DEFAULT_ORIENTATION;
    }

    private MovementType buildMovementType(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerWithOneGoForward.class)) {
            return GO_FORWARD_MOVEMENT;
        }
        return DEFAULT_EMPTY_MOVEMENT;
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
        return DEFAULT_ONE_TWO_COORDINATES;
    }

    private Explorer buildExplorer(MovementType movementType, Coordinates coordinates, Name name, OrientationType orientationType) {
        return Explorer.of(
                name,
                coordinates,
                new Orientation(orientationType),
                buildRawMovements(movementType)
        );
    }

    private String buildRawMovements(MovementType movementType) {
        if (movementType == null) {
            return "";
        }
        return movementType.name();
    }

}
