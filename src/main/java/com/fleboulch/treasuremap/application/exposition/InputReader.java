package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.app.TreasureQuestRunner;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.utils.CsvConverter;
import com.fleboulch.treasuremap.application.exposition.utils.CsvWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputReader {

    private final Logger log = LoggerFactory.getLogger(InputReader.class);

    private final TreasureQuestRunner treasureQuestRunner;

    public InputReader(TreasureQuestRunner treasureQuestRunner) {
        this.treasureQuestRunner = treasureQuestRunner;
    }

    public List<String> process(String filePath, String description) {

        TreasureQuest treasureQuest = buildTreasureQuestFromCsv(filePath);
        TreasureQuest finalTreasureQuest = treasureQuestRunner.start(treasureQuest);

        return csvOutput(finalTreasureQuest, description);

    }

    private TreasureQuest buildTreasureQuestFromCsv(String filePath) {
        List<String> configurationsWithoutComments = CsvConverter.toConfigurationList(filePath);
        return ApplicationFactory.toDomain(configurationsWithoutComments);
    }

    private List<String> csvOutput(TreasureQuest treasureQuest, String description) {
        List<String> exposition = ApplicationFactory.toExposition(treasureQuest);
        String outputPathName = CsvWriter.csvOutput(exposition, description);
        log.info("'{}' output has been generated into '{}'", description, outputPathName);

        return exposition;
    }

}
