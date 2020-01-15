package com.fleboulch.treasuremap.application.exposition;

import java.io.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InputReader {

    public static final String DELIMITER = " - ";
    public static final String SKIPPED_CHARACTER = "#";

    public InputReader() {
    }

    public void readFile(String filePath) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {

            br.lines()
                    .filter(line -> !line.startsWith(SKIPPED_CHARACTER))
                    .map(mapToItem)
                    .collect(Collectors.toList());

        } catch (IOException e) {
        }

    }

    private Function<String, String> mapToItem = (line) -> {
        String[] row = line.split(DELIMITER);// a CSV has comma separated lines

        System.out.println(row[0] + " - " + row[1] + " - " + row[2]);

//        String type = row[0];
//        Dimension dimension = null;
//        if (Objects.equals(type, "C")) {
//            int width = ConverterUtils.toInt(row[1]);
//            int height = ConverterUtils.toInt(row[2]);
//            dimension = new Dimension(new Width(width), new Height(height));
//        }
//
//        Map map = new Map(dimension, emptyList(), emptyList());
//        System.out.println(map);

        return row[0];
    };
}
