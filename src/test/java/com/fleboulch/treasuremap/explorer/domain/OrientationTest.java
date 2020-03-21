package com.fleboulch.treasuremap.explorer.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrientationTest {

    @Test
    void turn_left() {
        Orientation beginOrientation = new Orientation(OrientationType.N);
        Orientation finalOrientation = beginOrientation.leftTurn();

        assertThat(finalOrientation).isEqualTo(new Orientation(OrientationType.W));
    }

    @Test
    void turn_right() {
        Orientation beginOrientation = new Orientation(OrientationType.N);
        Orientation finalOrientation = beginOrientation.rightTurn();

        assertThat(finalOrientation).isEqualTo(new Orientation(OrientationType.E));
    }

}
