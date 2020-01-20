package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.Height;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.map.domain.Width;
import com.fleboulch.treasuremap.resolvers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ExplorerResolver.class)
class TreasureQuestRunnerTest {

    private static final Dimension DIMENSION = new Dimension(new Width(3), new Height(4));

    private TreasureQuestRunner runner;

    @BeforeEach
    void setup() {
        runner = new TreasureQuestRunner();
    }

    @Test
    void simple_quest_with_one_explorer(
            @ExplorerZeroZeroCoordinates @ExplorerSouthOrientation Explorer beginExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> historyExplorers = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(historyExplorers).containsExactly(beginExplorer);

    }

    @Test
    void simple_quest_with_one_explorer_and_go_forward_movement(
            @ExplorerWithOneGoForward @ExplorerZeroZeroCoordinates @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroOneCoordinates @ExplorerSouthOrientation Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);

    }

    private TreasureQuest buildSimpleQuest(Explorer beginExplorer) {
        return new TreasureQuest(
                new TreasureMap(DIMENSION, emptyList(), emptyList()),
                new Explorers(List.of(
                        beginExplorer
                ))
        );
    }

//    private Explorer buildLauraExplorer(Coordinates explorerCoordinates, String rawMovements) {
//        return Explorer.of(
//                new Name("Laura"),
//                explorerCoordinates,
//                new Orientation(OrientationType.S),
//                rawMovements
//        );
//    }

}
