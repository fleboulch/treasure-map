package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class InputReader {

    public static final String SKIPPED_CHARACTER = "#";

    public InputReader() {
    }

    public TreasureQuest readFile(String filePath) {

        List<String> configurations = getConfigurationFrom(filePath);
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(configurations);

        return treasureQuest;

    }

    private List<String> getConfigurationFrom(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {

            return br.lines()
                    .filter(line -> !line.startsWith(SKIPPED_CHARACTER))
//                    .map(mapToItem)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return emptyList();
    }

//    private Function<String, String> mapToItem = (line) -> {
//
//    };
}
