package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.Explorers;
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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class TreasureQuestRunnerTest {

    private static final Dimension DIMENSION = new Dimension(new Width(3), new Height(4));

    private TreasureQuestRunner runner;

    @BeforeEach
    void setup() {
        runner = new TreasureQuestRunner();
    }

    @Test
    void simple_quest() {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(0, 0, "");
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        assertThat(inputTreasureQuest).isEqualTo(finalQuest);

    }

    private TreasureQuest buildSimpleQuest(int x, int y, String rawMovements) {
        return new TreasureQuest(
                new TreasureMap(DIMENSION, emptyList(), emptyList()),
                new Explorers(List.of(
                        Explorer.of(
                                new Name("Laura"),
                                Coordinates.of(x, y),
                                new Orientation(OrientationType.S),
                                rawMovements
                        )
                ))
        );
    }

}
