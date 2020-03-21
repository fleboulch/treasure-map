package com.fleboulch.treasuremap.resolvers;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;

public class ExplorerResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Explorer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        if (!parameterContext.isAnnotated(ExplorerConfiguration.class)) {
            return buildExplorer(new Name("Laura"), OrientationType.E, Coordinates.of(1, 2), emptyList(), 0);
        }

        Name name = buildCustomName(parameterContext);
        OrientationType orientationType = buildOrientationType(parameterContext);
        Coordinates coordinates = buildCustomCoordinates(parameterContext);
        List<MovementType> movements = buildMovementsType(parameterContext);
        int nbTreasures = buildNbTreasures(parameterContext);

        return buildExplorer(name, orientationType, coordinates, movements, nbTreasures);
    }

    private Name buildCustomName(ParameterContext parameterContext) {
        return new Name(parameterContext.findAnnotation(ExplorerConfiguration.class).get().name());
    }

    private OrientationType buildOrientationType(ParameterContext parameterContext) {
        return parameterContext.findAnnotation(ExplorerConfiguration.class).get().orientationType();
    }

    private Coordinates buildCustomCoordinates(ParameterContext parameterContext) {
        return Coordinates.of(
                parameterContext.findAnnotation(ExplorerConfiguration.class).get().xCoordinates(),
                parameterContext.findAnnotation(ExplorerConfiguration.class).get().yCoordinates()
        );
    }

    private List<MovementType> buildMovementsType(ParameterContext parameterContext) {
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

    private int buildNbTreasures(ParameterContext parameterContext) {
        return parameterContext.findAnnotation(ExplorerConfiguration.class).get().nbTreasures();
    }

    private Explorer buildExplorer(Name name, OrientationType orientationType, Coordinates coordinates, List<MovementType> movementsType, int nbTreasures) {
        Explorer explorer = Explorer.of(
                name,
                new Position(
                        new Orientation(orientationType),
                        coordinates
                ),
                movementsType
        );

        IntStream.range(0, nbTreasures).forEach(index -> explorer.collectTreasure());

        return explorer;
    }

}
