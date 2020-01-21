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

    @BeforeEach
    void setup() {
        inputReader = new InputReader(treasureQuestRunner);
    }

    @Test
    void it_should_process_simple_quest(Explorer explorer) throws IOException {
        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(buildHistoryTreasureQuest(explorer));
        List<String> response = inputReader.process(buildCsvPath("quest.csv"));

        String caret = ApplicationFactory.CARET_DELIMITER;
        assertThat(response).containsExactly(
                String.format("C%s3%s4", caret, caret),
                String.format("M%s1%s1", caret, caret),
                String.format("T%s2%s2%s1", caret, caret, caret),
                String.format("A%sLaura%s1%s2%sE%s0", caret, caret, caret, caret, caret)
        );
    }

    @Test
    void it_should_process_quest_with_one_treasure(
            @ExplorerTwoOneCoordinates @ExplorerSouthOrientation @ExplorerWithOneGoForward Explorer explorer
    ) throws IOException {
        when(treasureQuestRunner.start(any(TreasureQuest.class))).thenReturn(buildHistoryTreasureQuest(explorer));
        List<String> response = inputReader.process(buildCsvPath("quest.csv"));

        String caret = ApplicationFactory.CARET_DELIMITER;
        assertThat(response).containsExactly(
                String.format("C%s3%s4", caret, caret),
                String.format("M%s1%s1", caret, caret),
                String.format("T%s2%s2%s1", caret, caret, caret),
                String.format("A%sLaura%s2%s2%sS%s1", caret, caret, caret, caret, caret)
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
