package com.fleboulch.treasuremap.shared.coordinates.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeAttributeException;
import com.fleboulch.treasuremap.shared.coordinates.domain.Axis;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AxisTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    void axis_with_valid_index_should_be_created(int index) {
        Axis axis = new Axis(index);
        assertThat(axis.index()).isEqualTo(index);
    }

    @Test
    void axis_cannot_be_created_with_negative_index() {

        assertThatThrownBy(
                () -> new Axis(-1)
        ).isInstanceOf(NegativeAttributeException.class);
    }

    @Test
    void two_axis_with_same_index_should_be_equal() {
        int index = 4;
        Axis axis1 = new Axis(index);
        Axis axis2 = new Axis(index);
        assertThat(axis1).isEqualTo(axis2);
    }

    @Test
    void two_axis_with_same_index_should_have_same_hash() {
        int index = 4;
        Axis axis1 = new Axis(index);
        Axis axis2 = new Axis(index);
        assertThat(axis1).hasSameHashCodeAs(axis2);
    }
}
