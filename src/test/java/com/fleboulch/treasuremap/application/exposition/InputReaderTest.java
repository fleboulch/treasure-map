package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.app.TreasureQuestRunner;
import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.resolvers.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, ExplorerResolver.class})
class InputReaderTest {

    private InputReader inputReader;

    @Mock
    private TreasureQuestRunner treasureQuestRunner;

    private static final String CARET = ApplicationFactory.CARET_DELIMITER.trim();

    @BeforeEach
    void setup() {
        inputReader = new InputReader(treasureQuestRunner);
    }

    @Test
    void it_should_process_simple_quest(Explorer explorer) throws IOException {
        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(buildHistoryTreasureQuest(explorer));
        List<String> response = inputReader.process(buildCsvPath("quest.csv"));

        assertThat(response).containsExactly(
                String.format("C %s 3 %s 4", CARET, CARET),
                String.format("M %s 1 %s 1", CARET, CARET),
                String.format("T %s 2 %s 2 %s 1", CARET, CARET, CARET),
                String.format("A %s Laura %s 1 %s 2 %s E %s 0", CARET, CARET, CARET, CARET, CARET)
        );
    }

    @Test
    void it_should_process_quest_with_one_treasure(
            @ExplorerTwoOneCoordinates @ExplorerSouthOrientation @ExplorerWithOneGoForward Explorer explorer,
            @ExplorerTwoTwoCoordinates @ExplorerSouthOrientation @ExplorerWithOneTreasure Explorer firstMoveExplorer
    ) throws IOException {
        HistoryTreasureQuest historyTreasureQuest = buildHistoryTreasureQuest(explorer);
        historyTreasureQuest.registerMove(firstMoveExplorer);
        historyTreasureQuest.removeOneTreasure(Coordinates.of(2, 2));

        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(historyTreasureQuest);
        List<String> response = inputReader.process(buildCsvPath("quest.csv"));

        assertThat(response).containsExactly(
                String.format("C %s 3 %s 4", CARET, CARET),
                String.format("M %s 1 %s 1", CARET, CARET),
                String.format("A %s Laura %s 2 %s 2 %s S %s 1", CARET, CARET, CARET, CARET, CARET)
        );
    }

    private HistoryTreasureQuest buildHistoryTreasureQuest(Explorer explorer) {
        return HistoryTreasureQuest.of(buildTreasureQuest(explorer));
    }

    private TreasureQuest buildTreasureQuest(Explorer explorer) {
        return new TreasureQuest(buildTreasureMap(), buildExplorers(explorer));
    }

    private Explorers buildExplorers(Explorer explorer) {
        return new Explorers(List.of(explorer));
    }

    private TreasureMap buildTreasureMap() {
        return new TreasureMap(buildDimension(), List.of(buildMountain(1, 1)), List.of(buildTreasure(2, 2, 1)));
    }

    private TreasureBox buildTreasure(int x, int y, int nbTreasures) {
        return new TreasureBox(Coordinates.of(x, y), nbTreasures);
    }

    private Dimension buildDimension() {
        return new Dimension(new Width(3), new Height(4));
    }

    private MountainBox buildMountain(int x, int y) {
        return new MountainBox(Coordinates.of(x, y));
    }

    // duplicated code
    private String buildCsvPath(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName, this.getClass());
        return classPathResource.getFile().getPath();
    }
}
