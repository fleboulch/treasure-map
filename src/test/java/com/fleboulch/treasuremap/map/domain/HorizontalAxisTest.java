package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeAttributeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HorizontalAxisTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    void horizontal_axis_with_valid_index_should_be_created(int index) {
        Axis axis = new HorizontalAxis(index);
        assertThat(axis.index()).isEqualTo(index);
    }

    @Test
    void horizontal_axis_cannot_be_created_with_negative_index() {

        assertThatThrownBy(
                () -> new HorizontalAxis(-1)
        ).isInstanceOf(NegativeAttributeException.class);
    }

}
