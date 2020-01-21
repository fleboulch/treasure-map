package com.fleboulch.treasuremap.application.exposition.utils;

import com.fleboulch.treasuremap.application.exposition.ApplicationFactory;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CsvWriter {

    public static final String OUTPUT_DIRECTORY = "output/";
    public static final String FILENAME = "output.csv";

    private CsvWriter() {
    }

    public static void csvOutput(List<String> lines) {
        File file = new File(OUTPUT_DIRECTORY + FILENAME);

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {

            List<String[]> linesAsArray = buildArray(lines);
            writer.writeAll(linesAsArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String[]> buildArray(List<String> lines) {
        return lines.stream()
                .map(CsvWriter::buildData)
                .collect(toList());
    }

    private static String[] buildData(String rawLine) {
        return rawLine.split(ApplicationFactory.CARET_DELIMITER);
    }

}
