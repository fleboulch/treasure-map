package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeIndexException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerticalAxisTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    void vertical_axis_with_valid_index_should_be_created(int index) {
        Axis axis = new VerticalAxis(index);
        assertThat(axis.index()).isEqualTo(index);
    }

    @Test
    void vertical_axis_cannot_be_created_with_negative_index() {

        assertThatThrownBy(
                () -> new VerticalAxis(-1)
        ).isInstanceOf(NegativeIndexException.class);
    }
}
