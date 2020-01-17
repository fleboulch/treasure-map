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

        if (parameterContext.isAnnotated(ExplorerWithOneGoForward.class)) {
            return buildExplorer(MovementType.A, ONE_TWO_COORDINATES, LAURA);
        } else if (parameterContext.isAnnotated(ExplorerZeroZeroCoordinates.class)) {
            return buildExplorer(null, ZERO_ZERO_COORDINATES, LAURA);
        } else if (parameterContext.isAnnotated(ExplorerMichelWithOneOneCoordinates.class)) {
            return buildExplorer(null, ONE_ONE_COORDINATES, "Michel");
        } else if (parameterContext.isAnnotated(ExplorerAlbertoWithZeroOneCoordinates.class)) {
            return buildExplorer(null, ZERO_ONE_COORDINATES, "Alberto");
        }

        return buildExplorer(movementType, coordinates, name);
    }

    private Explorer buildExplorer(MovementType movementType, Coordinates coordinates, String name) {
        return Explorer.of(
                new Name(name),
                coordinates,
                new Orientation(OrientationType.E),
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
