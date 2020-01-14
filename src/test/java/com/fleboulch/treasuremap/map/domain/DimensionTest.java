package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.kernel.exceptions.NegativeIndexException;
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
        Dimension dimension = new Dimension(new Width(width), new Height(height));
        assertThat(dimension.width().value()).isEqualTo(width);
        assertThat(dimension.height().value()).isEqualTo(height);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "-2,-2",
            "-4,3",
            "3,-4"
    })
    void a_dimension_with_negative_width_or_height_cannot_be_created(int width, int height) {

        assertThatThrownBy(
                () -> new Dimension(new Width(width), new Height(height))
        ).isInstanceOf(NegativeIndexException.class);
    }

    @Test
    void a_dimension_with_null_width_and_height_cannot_be_created() {

        assertThatThrownBy(
                () -> new Dimension(null, null)
        ).isInstanceOf(DomainException.class);
    }

}
