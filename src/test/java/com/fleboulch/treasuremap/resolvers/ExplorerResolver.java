package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ExplorerResolver implements ParameterResolver {

    // default values
    private static final Name DEFAULT_NAME = new Name("Laura");
    private static final OrientationType DEFAULT_ORIENTATION = OrientationType.E;
    private static final Coordinates DEFAULT_ONE_TWO_COORDINATES = Coordinates.of(1, 2);

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
        if (parameterContext.isAnnotated(ExplorerConfiguration.class)) {
            return parameterContext.findAnnotation(ExplorerConfiguration.class).get().nbTreasures();
        }
        return 0;
    }

    private OrientationType buildOrientationType(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerConfiguration.class)) {
            return parameterContext.findAnnotation(ExplorerConfiguration.class).get().orientationType();
        }
        return DEFAULT_ORIENTATION;
    }

    private List<MovementType> buildMovementsType(ParameterContext parameterContext) {
        List<MovementType> movements = new ArrayList<>();
        if (parameterContext.isAnnotated(ExplorerConfiguration.class)) {
            boolean isExampleSequenceMovements = parameterContext.findAnnotation(ExplorerConfiguration.class).get().isExampleSequenceMovements();
            if (isExampleSequenceMovements) {
                return Arrays.asList(
                        MovementType.A,
                        MovementType.A,
                        MovementType.D,
                        MovementType.A,
                        MovementType.D,
                        MovementType.A,
                        MovementType.G,
                        MovementType.G,
                        MovementType.A
                );
            } else {
                return Arrays.asList(parameterContext.findAnnotation(ExplorerConfiguration.class).get().movements());
            }
        }

        return movements;
    }

    private Name buildCustomName(ParameterContext parameterContext) {
        String name = DEFAULT_NAME.value();
        if (parameterContext.isAnnotated(ExplorerConfiguration.class)) {
            name = parameterContext.findAnnotation(ExplorerConfiguration.class).get().name();
        }
        return new Name(name);
    }

    private Coordinates buildCustomCoordinates(ParameterContext parameterContext) {
        if (parameterContext.isAnnotated(ExplorerConfiguration.class)) {
            return Coordinates.of(
                    parameterContext.findAnnotation(ExplorerConfiguration.class).get().xCoordinates(),
                    parameterContext.findAnnotation(ExplorerConfiguration.class).get().yCoordinates()
            );
        }

        return DEFAULT_ONE_TWO_COORDINATES;
    }

    private Explorer buildExplorer(List<MovementType> movementsType, Coordinates coordinates, Name name, OrientationType orientationType, int nbTreasures) {
        Explorer explorer = Explorer.of(
                name,
                coordinates,
                new Orientation(orientationType),
                movementsType
        );

        IntStream.range(0, nbTreasures).forEach(index -> explorer.collectTreasure());

        return explorer;
    }

}
