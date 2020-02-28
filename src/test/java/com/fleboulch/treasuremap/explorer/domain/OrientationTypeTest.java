package com.fleboulch.treasuremap.explorer.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrientationTypeTest {

    @ParameterizedTest
    @CsvSource({
            "N,E",
            "E,S",
            "S,W",
            "W,N",
    })
    void turn_right_should_return_orientation_with_90_angle(String beginOrientation, String finalOrientation) {
        OrientationType newOrientationType = OrientationType.valueOf(beginOrientation).turnRight();

        assertThat(newOrientationType).isEqualByComparingTo(OrientationType.valueOf(finalOrientation));
    }

    @ParameterizedTest
    @CsvSource({
            "N,W",
            "W,S",
            "S,E",
            "E,N",
    })
    void turn_left_should_return_orientation_with_90_angle(String beginOrientation, String finalOrientation) {
        OrientationType newOrientationType = OrientationType.valueOf(beginOrientation).turnLeft();

        assertThat(newOrientationType).isEqualByComparingTo(OrientationType.valueOf(finalOrientation));
    }

}
