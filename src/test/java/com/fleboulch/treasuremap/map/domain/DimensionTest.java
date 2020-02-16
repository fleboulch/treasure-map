package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.kernel.exceptions.NegativeAttributeException;
import com.fleboulch.treasuremap.kernel.exceptions.NegativeOrZeroAttributeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DimensionTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2,2",
            "4,3",
            "3,4"
    })
    void a_dimension_should_have_positive_width_and_height(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        assertThat(dimension.width()).isEqualTo(width);
        assertThat(dimension.height()).isEqualTo(height);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0",
            "0,5",
            "5,0",
            "-2,-2",
            "-4,3",
            "3,-4"
    })
    void a_dimension_with_negative_or_zero_width_or_height_cannot_be_created(int width, int height) {

        assertThatThrownBy(
                () -> new Dimension(width, height)
        ).isInstanceOf(NegativeOrZeroAttributeException.class);
    }

}
