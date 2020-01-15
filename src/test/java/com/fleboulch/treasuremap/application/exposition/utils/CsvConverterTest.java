package com.fleboulch.treasuremap.application.exposition.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvConverterTest {

    public static final String BASE_PATH = "/home/florent/dev/personal/technical-tests/carbon-it/treasure-map/src/test/resources/com/fleboulch/treasuremap/application/exposition/";

    @Test
    void it_should_exclude_commented_lines_from_csv_and_correctly_convert_simple_csv() {
        List<String> config = CsvConverter.toConfigurationList(BASE_PATH + "simple-quest.csv");

        assertThat(config).hasSize(1);
        assertThat(config.get(0)).isEqualTo("C - 3 - 4");
    }

    @Test
    void it_should_exclude_commented_lines_from_csv_and_correctly_convert_advanced_csv() {
        List<String> config = CsvConverter.toConfigurationList(BASE_PATH + "quest-with-mountain-and-treasure.csv");

        assertThat(config).hasSize(3);
        assertThat(config.get(0)).isEqualTo("C - 3 - 4");
        assertThat(config.get(1)).isEqualTo("M - 1 - 1");
        assertThat(config.get(2)).isEqualTo("T - 2 - 1 - 1");
    }

}
