package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.app.TreasureQuestRunner;
import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.resolvers.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
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
        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(buildHistoryTreasureQuest(explorer, List.of(buildMountain(1, 1)), List.of(buildTreasure(2, 2, 1))));
        List<String> response = inputReader.process(buildCsvPath("quest.csv"), "simple quest");

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
        HistoryTreasureQuest historyTreasureQuest = buildHistoryTreasureQuest(explorer, List.of(buildMountain(1, 1)), List.of(buildTreasure(2, 2, 1)));
        historyTreasureQuest.registerMove(firstMoveExplorer);
        historyTreasureQuest.removeOneTreasure(Coordinates.of(2, 2));

        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(historyTreasureQuest);
        List<String> response = inputReader.process(buildCsvPath("quest.csv"), "quest with one treasure");

        assertThat(response).containsExactly(
                String.format("C %s 3 %s 4", CARET, CARET),
                String.format("M %s 1 %s 1", CARET, CARET),
                String.format("A %s Laura %s 2 %s 2 %s S %s 1", CARET, CARET, CARET, CARET, CARET)
        );
    }

    @Test
    void it_should_process_example_quest(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation @ExplorerWithExampleSequenceMovements Explorer explorer
    ) throws IOException {
        HistoryTreasureQuest historyTreasureQuest = buildHistoryTreasureQuest(
                explorer,
                List.of(buildMountain(1, 0), buildMountain(2, 1)),
                List.of(buildTreasure(0, 3, 2), buildTreasure(1, 3, 3))
        );

        Coordinates finalCoordinates = Coordinates.of(0, 3);

        Explorer finalExplorer = Explorer.of(explorer.name(), finalCoordinates, explorer.orientation(), emptyList());

        finalExplorer.collectTreasure();
        finalExplorer.collectTreasure();
        finalExplorer.collectTreasure();
        historyTreasureQuest.registerMove(finalExplorer);

        Coordinates treasureCoordinates1 = Coordinates.of(1, 3);
        historyTreasureQuest.removeOneTreasure(treasureCoordinates1);
        historyTreasureQuest.removeOneTreasure(finalCoordinates);
        historyTreasureQuest.removeOneTreasure(finalCoordinates);

        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(historyTreasureQuest);
        List<String> response = inputReader.process(buildCsvPath("quest.csv"), "example quest");

        assertThat(response).containsExactly(
                String.format("C %s 3 %s 4", CARET, CARET),
                String.format("M %s 1 %s 0", CARET, CARET),
                String.format("M %s 2 %s 1", CARET, CARET),
                String.format("T %s 1 %s 3 %s 2", CARET, CARET, CARET),
                String.format("A %s Laura %s 0 %s 3 %s S %s 3", CARET, CARET, CARET, CARET, CARET)
        );
    }

    private HistoryTreasureQuest buildHistoryTreasureQuest(Explorer explorer, List<MountainBox> mountainBoxes, List<TreasureBox> treasureBoxes) {
        return HistoryTreasureQuest.of(buildTreasureQuest(explorer, mountainBoxes, treasureBoxes));
    }

    private TreasureQuest buildTreasureQuest(Explorer explorer, List<MountainBox> mountainBoxes, List<TreasureBox> treasureBoxes) {
        return new TreasureQuest(buildTreasureMap(mountainBoxes, treasureBoxes), buildExplorers(explorer));
    }

    private Explorers buildExplorers(Explorer explorer) {
        return new Explorers(List.of(explorer));
    }

    private TreasureMap buildTreasureMap(List<MountainBox> mountainBoxes, List<TreasureBox> treasureBoxes) {
        return new TreasureMap(buildDimension(), mountainBoxes, treasureBoxes);
    }

    private TreasureBox buildTreasure(int x, int y, int nbTreasures) {
        return new TreasureBox(Coordinates.of(x, y), nbTreasures);
    }

    private Dimension buildDimension() {
        return new Dimension(3, 4);
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
