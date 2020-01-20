package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.app.TreasureQuestRunner;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InputReaderTest {

    private InputReader inputReader;

    @Mock
    private TreasureQuestRunner treasureQuestRunner;

    @BeforeEach
    void setup() {
        inputReader = new InputReader(treasureQuestRunner);
    }

    @Test
    void it_should_process() throws IOException {
        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(any());
        inputReader.process(buildCsvPath("quest.csv"));
    }

    // duplicated code
    private String buildCsvPath(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName, this.getClass());
        return classPathResource.getFile().getPath();
    }
}
