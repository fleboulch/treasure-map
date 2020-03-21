package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MovementTypeTest {

    @ParameterizedTest
    @CsvSource({
            "E, N",
            "N, W",
            "W, S",
            "S, E"
    })
    void G_movement_should_turn_to_the_left(String beginOrientation, String finalOrientation) {
        Position nextPosition = MovementType.G.executeMovement(buildZeroZeroPosition(OrientationType.valueOf(beginOrientation)));

        assertThat(nextPosition).isEqualTo(buildZeroZeroPosition(OrientationType.valueOf(finalOrientation)));
    }

    @ParameterizedTest
    @CsvSource({
            "N, E",
            "W, N",
            "S, W",
            "E, S"
    })
    void D_movement_should_turn_to_the_left(String beginOrientation, String finalOrientation) {
        Position nextPosition = MovementType.D.executeMovement(buildZeroZeroPosition(OrientationType.valueOf(beginOrientation)));

        assertThat(nextPosition).isEqualTo(buildZeroZeroPosition(OrientationType.valueOf(finalOrientation)));
    }

    @ParameterizedTest
    @CsvSource({
            "S, 0, 0, 0, 1",
            "E, 0, 0, 1, 0",
            "N, 1, 1, 1, 0",
            "W, 1, 1, 0, 1"
    })
    void A_movement_should_turn_to_the_left(String beginOrientation, int beginX, int beginY, int finalX, int finalY) {
        Position nextPosition = MovementType.A.executeMovement(buildPosition(OrientationType.valueOf(beginOrientation), Coordinates.of(beginX, beginY)));

        assertThat(nextPosition).isEqualTo(buildPosition(OrientationType.valueOf(beginOrientation), Coordinates.of(finalX, finalY)));
    }

    private Position buildZeroZeroPosition(OrientationType orientationType) {
        return new Position(new Orientation(orientationType), Coordinates.of(0, 0));
    }

    private Position buildPosition(OrientationType orientationType, Coordinates coordinates) {
        return new Position(new Orientation(orientationType), coordinates);
    }

}
