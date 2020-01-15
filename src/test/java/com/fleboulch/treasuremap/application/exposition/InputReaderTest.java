package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputReaderTest {

    InputReader inputReader;

    @BeforeEach
    void init() {
        inputReader = new InputReader(treasureQuestRunner);
    }

    @Test
    void it() {
        String basePath = "/home/florent/dev/personal/technical-tests/carbon-it/treasure-map/src/test/resources/com/fleboulch/treasuremap/application/exposition/";
        TreasureQuest treasureQuest = inputReader.process(basePath + "simple-quest.csv");

        System.out.println(treasureQuest);
    }

}
