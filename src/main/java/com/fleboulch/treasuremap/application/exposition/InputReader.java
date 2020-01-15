package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.app.TreasureQuestRunner;
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

    public TreasureQuest process(String filePath) {

        List<String> configurations = CsvConverter.toConfigurationList(filePath);
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(configurations);
        treasureQuestRunner.start(treasureQuest);

        return treasureQuest;

    }


}
