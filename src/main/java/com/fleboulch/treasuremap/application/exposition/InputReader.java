package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.utils.CsvConverter;

import java.util.List;

public class InputReader {


    public InputReader() {
    }

    public TreasureQuest readFile(String filePath) {

        List<String> configurations = CsvConverter.toConfigurationList(filePath);
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(configurations);

        return treasureQuest;

    }


}
