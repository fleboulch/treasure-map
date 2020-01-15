package com.fleboulch.treasuremap.kernel.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConverterUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "1",
            "2",
            " 3 "
    })
    void it_should_successfully_convert_string_to_int(String inputValue) {
        int result = ConverterUtils.toInt(inputValue);

        assertThat(result).isInstanceOf(Integer.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a",
            "1.3",
            "1,3"
    })
    void it_should_failed_to_convert_string_to_int(String inputValue) {
        assertThatThrownBy(() ->
                ConverterUtils.toInt(inputValue)
        ).isInstanceOf(NumberFormatException.class);
    }

}
