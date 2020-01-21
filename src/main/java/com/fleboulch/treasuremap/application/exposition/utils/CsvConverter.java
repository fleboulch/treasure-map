package com.fleboulch.treasuremap.application.exposition.utils;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class CsvConverter {

    static final String SKIPPED_CHARACTER = "#";

    private CsvConverter() {
    }

    public static List<String> toConfigurationList(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {

            return br.lines()
                    .filter(line -> !line.startsWith(SKIPPED_CHARACTER))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptyList();
    }

}
