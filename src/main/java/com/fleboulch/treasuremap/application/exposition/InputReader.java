package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.app.TreasureQuestRunner;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.utils.CsvConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputReader {

    private final TreasureQuestRunner treasureQuestRunner;

    public InputReader(TreasureQuestRunner treasureQuestRunner) {
        this.treasureQuestRunner = treasureQuestRunner;
    }

    public List<String> process(String filePath) {

        TreasureQuest treasureQuest = buildTreasureQuestFromCsv(filePath);
        HistoryTreasureQuest historyQuest = treasureQuestRunner.start(treasureQuest);

        return ApplicationFactory.toExposition(historyQuest);

    }

    private TreasureQuest buildTreasureQuestFromCsv(String filePath) {
        List<String> configurationsWithoutComments = CsvConverter.toConfigurationList(filePath);
        return ApplicationFactory.toDomain(configurationsWithoutComments);
    }

}
