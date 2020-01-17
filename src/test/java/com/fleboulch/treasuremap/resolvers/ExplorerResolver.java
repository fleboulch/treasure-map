package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.extension.*;

public class ExplorerResolver implements ParameterResolver {

    public static final Coordinates ONE_TWO_COORDINATES = Coordinates.of(1, 2);
    public static final Coordinates ZERO_ZERO_COORDINATES = Coordinates.of(0, 0);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Explorer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.isAnnotated(ExplorerWithOneGoForward.class)) {
            return buildExplorer(MovementType.A, ONE_TWO_COORDINATES);
        } else if (parameterContext.isAnnotated(ExplorerZeroZeroCoordinates.class)) {
            return buildExplorer(null, ZERO_ZERO_COORDINATES);
        }

        return buildExplorer(null, ONE_TWO_COORDINATES);
    }

    private Explorer buildExplorer(MovementType movementType, Coordinates coordinates) {
        return Explorer.of(
                new Name("Laura"),
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
