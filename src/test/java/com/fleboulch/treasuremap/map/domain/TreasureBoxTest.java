package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeAttributeException;
import com.fleboulch.treasuremap.kernel.exceptions.NegativeOrZeroAttributeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TreasureBoxTest {

    public static final int HORIZONTAL_AXIS = 4;
    public static final int VERTICAL_AXIS = 3;

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void treasure_box_with_positive_number_of_treasers_should_be_created(int nbTreasures) {
        TreasureBox treasureBox = buildTreasureBox(nbTreasures);
        assertThat(treasureBox.x().index()).isEqualTo(HORIZONTAL_AXIS);
        assertThat(treasureBox.y().index()).isEqualTo(VERTICAL_AXIS);
        assertThat(treasureBox.nbTreasures()).isEqualTo(nbTreasures);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -2})
    void treasure_box_with_zero_or_negative_number_of_treasers_cannot_be_created(int nbTreasures) {

        assertThatThrownBy(
                () -> buildTreasureBox(nbTreasures)
        ).isInstanceOf(NegativeOrZeroAttributeException.class);
    }

    private TreasureBox buildTreasureBox(int nbTreasures) {
        return new TreasureBox(new HorizontalAxis(HORIZONTAL_AXIS), new VerticalAxis(VERTICAL_AXIS), nbTreasures);
    }

}
