package com.fleboulch.treasuremap.application.exposition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputReaderTest {

    InputReader inputReader;

    @BeforeEach
    void init() {
        inputReader = new InputReader();
    }

    @Test
    void it() {
        String basePath = "/home/florent/dev/personal/technical-tests/carbon-it/treasure-map/src/test/resources/com/fleboulch/treasuremap/application/exposition/";
        inputReader.readFile(basePath + "simple-map.csv");
    }

}
