package com.fleboulch.treasuremap.application.exposition.utils;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvConverterTest {

    private static final String CSV_BASE_PATH = "com/fleboulch/treasuremap/application/exposition/utils/";

    @Test
    void it_should_exclude_commented_lines_from_csv_and_correctly_convert_simple_csv() throws IOException {
        String filePath = buildCsvPath("simple-quest.csv");

        List<String> config = CsvConverter.toConfigurationList(filePath);

        assertThat(config).hasSize(1);
        assertThat(config.get(0)).isEqualTo("C - 3 - 4");
    }

    @Test
    void it_should_exclude_commented_lines_from_csv_and_correctly_convert_advanced_csv() throws IOException {
        String filePath = buildCsvPath("quest-with-mountain-and-treasure.csv");

        List<String> config = CsvConverter.toConfigurationList(filePath);

        assertThat(config).hasSize(3);
        assertThat(config.get(0)).isEqualTo("C - 3 - 4");
        assertThat(config.get(1)).isEqualTo("M - 1 - 1");
        assertThat(config.get(2)).isEqualTo("T - 2 - 1 - 1");
    }

    private String buildCsvPath(String fileName) throws IOException {
        return new ClassPathResource(CSV_BASE_PATH + fileName).getFile().getPath();
    }

}
