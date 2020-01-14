package com.fleboulch.treasuremap.map.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DimensionTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2,2",
            "4,3",
            "3,4"
    })
    void a_dimension_should_have_positive_width_and_height(int width, int height) {
        Dimension dimension = new Dimension(new Width(width), new Height(height));
        Assertions.assertThat(dimension.width().value()).isEqualTo(width);
        Assertions.assertThat(dimension.height().value()).isEqualTo(height);

    }

}
