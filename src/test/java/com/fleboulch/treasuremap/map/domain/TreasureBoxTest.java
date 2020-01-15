package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeOrZeroAttributeException;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import com.fleboulch.treasuremap.shared.coordinates.domain.HorizontalAxis;
import com.fleboulch.treasuremap.shared.coordinates.domain.VerticalAxis;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TreasureBoxTest {

    public static final int HORIZONTAL_AXIS = 4;
    public static final int VERTICAL_AXIS = 3;

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void treasure_box_with_positive_number_of_treasers_should_be_created(int nbTreasures) {
        TreasureBox treasureBox = buildTreasureBox(HORIZONTAL_AXIS, VERTICAL_AXIS, nbTreasures);
        assertThat(treasureBox.coordinates().x().index()).isEqualTo(HORIZONTAL_AXIS);
        assertThat(treasureBox.coordinates().y().index()).isEqualTo(VERTICAL_AXIS);
        assertThat(treasureBox.nbTreasures()).isEqualTo(nbTreasures);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -2})
    void treasure_box_with_zero_or_negative_number_of_treasers_cannot_be_created(int nbTreasures) {

        assertThatThrownBy(
                () -> buildTreasureBox(HORIZONTAL_AXIS, VERTICAL_AXIS, nbTreasures)
        ).isInstanceOf(NegativeOrZeroAttributeException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0,1,1",
            "1,0,2,1",
            "1,1,2,2",
            "2,4,6,7",
    })
    void treasure_should_be_inside_dimension_if_dimension_is_equal_or_bigger_than_treasure_coordinates(int x, int y, int width, int height) {

        Dimension dimension = new Dimension(new Width(width), new Height(height));
        TreasureBox treasureBox = buildTreasureBox(x, y, 1);
        assertThat(treasureBox.isInside(dimension)).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,1,1,1",
            "2,2,2,1",
            "2,2,1,1"
    })
    void treasure_should_be_outside_dimension_if_dimension_is_smaller_than_treasure_coordinates(int x, int y, int width, int height) {

        Dimension dimension = new Dimension(new Width(width), new Height(height));
        TreasureBox treasureBox = buildTreasureBox(x, y, 1);
        assertThat(treasureBox.isInside(dimension)).isFalse();
    }


    private TreasureBox buildTreasureBox(int x, int y, int nbTreasures) {
        return new TreasureBox(Coordinates.of(x, y), nbTreasures);
    }

}
