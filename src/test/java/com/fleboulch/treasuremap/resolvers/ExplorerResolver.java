package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ExplorerResolver implements ParameterResolver {

    private static final Coordinates ONE_TWO_COORDINATES = Coordinates.of(1, 2);
    private static final Coordinates ZERO_ZERO_COORDINATES = Coordinates.of(0, 0);
    private static final Coordinates ONE_ONE_COORDINATES = Coordinates.of(1, 1);
    private static final Coordinates ZERO_ONE_COORDINATES = Coordinates.of(0, 1);
    private static final String LAURA = "Laura";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Explorer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        MovementType movementType = null;
        Coordinates coordinates = ONE_TWO_COORDINATES;
        String name = LAURA;

        OrientationType orientationType = buildOrientationType(parameterContext);
        movementType = buildMovementType(parameterContext, movementType);
        name = buildCustomName(parameterContext, name);
        coordinates = buildCustomCoordinates(parameterContext, coordinates);

        return buildExplorer(movementType, coordinates, name, orientationType);
    }

    private OrientationType buildOrientationType(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerSouthOrientation.class)) {
            return OrientationType.S;
        }
        return OrientationType.E;
    }

    private MovementType buildMovementType(ParameterContext parameterContext, MovementType movementType) {
        if (parameterContext.isAnnotated(ExplorerWithOneGoForward.class)) {
            movementType = MovementType.A;
        }
        return movementType;
    }

    private String buildCustomName(ParameterContext parameterContext, String name) {
        if (parameterContext.isAnnotated(ExplorerMichel.class)) {
            name = "Michel";
        }
        if (parameterContext.isAnnotated(ExplorerAlberto.class)) {
            name = "Alberto";
        }
        return name;
    }

    private Coordinates buildCustomCoordinates(ParameterContext parameterContext, Coordinates coordinates) {
        if (parameterContext.isAnnotated(ExplorerOneOneCoordinates.class)) {
            coordinates = ONE_ONE_COORDINATES;
        }
        if (parameterContext.isAnnotated(ExplorerZeroZeroCoordinates.class)) {
            coordinates = ZERO_ZERO_COORDINATES;
        }
        if (parameterContext.isAnnotated(ExplorerZeroOneCoordinates.class)) {
            coordinates = ZERO_ONE_COORDINATES;
        }
        return coordinates;
    }

    private Explorer buildExplorer(MovementType movementType, Coordinates coordinates, String name, OrientationType orientationType) {
        return Explorer.of(
                new Name(name),
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
