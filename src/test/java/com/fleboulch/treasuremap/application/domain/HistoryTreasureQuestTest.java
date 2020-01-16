package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.explorer.domain.Orientation;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.Height;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.map.domain.Width;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class HistoryTreasureQuestTest {

    public static final Explorer EXPLORER_1 = Explorer.of(
            new Name("Laura"),
            Coordinates.of(1, 2),
            new Orientation(OrientationType.E),
            ""
    );
    public static final Explorer EXPLORER_2 = Explorer.of(
            new Name("Michel"),
            Coordinates.of(1, 1),
            new Orientation(OrientationType.E),
            ""
    );
    public static final Explorer EXPLORER_3 = Explorer.of(
            new Name("Alberto"),
            Coordinates.of(0, 1),
            new Orientation(OrientationType.E),
            ""
    );

    @Test
    void build_history_quest() {
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(buildQuest());

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).hasSize(3);

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsOnlyKeys(
                EXPLORER_1.name(),
                EXPLORER_2.name(),
                EXPLORER_3.name()
        );

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(EXPLORER_1));
        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(EXPLORER_2));
        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(EXPLORER_3));
    }

    private TreasureQuest buildQuest() {
        return new TreasureQuest(
                new TreasureMap(new Dimension(new Width(4), new Height(5)), emptyList(), emptyList()),
                new Explorers(List.of(
                        EXPLORER_1,
                        EXPLORER_2,
                        EXPLORER_3
                ))
        );
    }

}
