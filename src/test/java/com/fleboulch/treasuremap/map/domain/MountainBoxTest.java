package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeAttributeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MountainBoxTest {

    public static final int HORIZONTAL_AXIS = 4;
    public static final int VERTICAL_AXIS = 3;

    @Test
    void mountain_box_with_valid_indexes_should_be_created() {
        MountainBox mountainBox = buildMountainBox(HORIZONTAL_AXIS, VERTICAL_AXIS);
        assertThat(mountainBox.x().index()).isEqualTo(HORIZONTAL_AXIS);
        assertThat(mountainBox.y().index()).isEqualTo(VERTICAL_AXIS);
    }

    @Test
    void mountain_box_with_negative_indexes_cannot_be_created() {

        assertThatThrownBy(
                () -> buildMountainBox(-1, -2)
        ).isInstanceOf(NegativeAttributeException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0,1,1",
            "1,0,2,1",
            "1,1,2,2",
            "2,4,6,7",
    })
    void mountain_should_be_inside_dimension_if_dimension_is_equal_or_bigger_than_mountain_coordinates(int x, int y, int width, int height) {

        Dimension dimension = new Dimension(new Width(width), new Height(height));
        MountainBox mountainBox = buildMountainBox(x, y);
        assertThat(mountainBox.isInside(dimension)).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,1,1,1",
            "2,2,2,1",
            "2,2,1,1"
    })
    void mountain_should_be_outside_dimension_if_dimension_is_smaller_than_mountain_coordinates(int x, int y, int width, int height) {

        Dimension dimension = new Dimension(new Width(width), new Height(height));
        MountainBox mountainBox = buildMountainBox(x, y);
        assertThat(mountainBox.isInside(dimension)).isFalse();
    }


    private MountainBox buildMountainBox(int horizontalAxis, int verticalAxis) {
        return new MountainBox(new HorizontalAxis(horizontalAxis), new VerticalAxis(verticalAxis));
    }
}
