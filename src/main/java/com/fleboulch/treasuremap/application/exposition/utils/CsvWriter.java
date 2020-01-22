package com.fleboulch.treasuremap.application.exposition.utils;

import com.fleboulch.treasuremap.application.exposition.ApplicationFactory;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class CsvWriter {

    private static final String OUTPUT_DIRECTORY = "output/";
    private static final String FILENAME = "output-";
    private static final String CSV_EXTENSION = ".csv";

    private CsvWriter() {
    }

    public static String csvOutput(List<String> lines, String description) {
        String outputPathName = buildOutputFilePathName();
        File file = new File(outputPathName);

        try (CSVWriter writer = new CSVWriter(
                new FileWriter(file),
                buildCaretSeparator(),
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END
        )) {

            addCommentIntoFile(description, writer);

            List<String[]> linesAsArray = buildArray(lines);
            writer.writeAll(linesAsArray);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputPathName;

    }

    private static void addCommentIntoFile(String description, CSVWriter writer) {
        String descriptionComment = String.format("# %s", description);
        writer.writeNext(new String[]{descriptionComment});
    }

    private static String buildOutputFilePathName() {
        return OUTPUT_DIRECTORY + FILENAME + UUID.randomUUID() + CSV_EXTENSION;
    }

    private static char buildCaretSeparator() {
        return ApplicationFactory.CARET_DELIMITER.trim().charAt(0);
    }

    private static List<String[]> buildArray(List<String> lines) {
        return lines.stream()
                .map(CsvWriter::buildData)
                .collect(toList());
    }

    private static String[] buildData(String rawLine) {
        return rawLine.split(ApplicationFactory.CARET_DELIMITER.trim());
    }

}
