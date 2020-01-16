package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.explorer.domain.Orientation;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.Height;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.map.domain.Width;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class TreasureQuestRunnerTest {

    private static final Dimension DIMENSION = new Dimension(new Width(3), new Height(4));
    public static final Coordinates ZERO_ZERO_COORDINATES = Coordinates.of(0, 0);
    public static final Coordinates ZERO_ONE_COORDINATES = Coordinates.of(0, 1);

    private TreasureQuestRunner runner;

    @BeforeEach
    void setup() {
        runner = new TreasureQuestRunner();
    }

    @Test
    void simple_quest_with_one_explorer() {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(ZERO_ZERO_COORDINATES, "");
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        Explorer explorer = inputTreasureQuest.explorers().explorers().get(0);
        List<Explorer> historyExplorers = finalQuest.historyMovementsPerExplorer().get(explorer.name());

        assertThat(historyExplorers).containsExactly(explorer);

    }

    @Test
    void simple_quest_with_one_explorer_and_go_forward_mouvement() {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(ZERO_ZERO_COORDINATES, "A");
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        Explorer explorer = inputTreasureQuest.explorers().explorers().get(0);
        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(explorer.name());

        Explorer finalExplorer = explorer.goForward();

        assertThat(explorerMovements).containsExactly(explorer, finalExplorer);

    }

    private TreasureQuest buildSimpleQuest(Coordinates explorerCoordinates, String rawMovements) {
        return new TreasureQuest(
                new TreasureMap(DIMENSION, emptyList(), emptyList()),
                new Explorers(List.of(
                        buildLauraExplorer(explorerCoordinates, rawMovements)
                ))
        );
    }

    private Explorer buildLauraExplorer(Coordinates explorerCoordinates, String rawMovements) {
        return Explorer.of(
                new Name("Laura"),
                explorerCoordinates,
                new Orientation(OrientationType.S),
                rawMovements
        );
    }

}
