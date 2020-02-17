package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.resolvers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
class HistoryTreasureQuestTest {

    @Test
    void build_history_quest(
            Explorer explorer1,
            @ExplorerConfiguration(name = "Michel", yCoordinates = 1) Explorer explorer2,
            @ExplorerConfiguration(name = "Alberto", xCoordinates = 0, yCoordinates = 1) Explorer explorer3
    ) {
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(buildQuest(List.of(
                explorer1,
                explorer2,
                explorer3
        )));

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).hasSize(3);

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsOnlyKeys(
                explorer1.name(),
                explorer2.name(),
                explorer3.name()
        );

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(explorer1));
        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(explorer2));
        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(explorer3));
    }

    @Test
    void register_move_for_existing_explorer_doing_the_quest(Explorer explorer) {
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(buildQuest(List.of(explorer)));
        historyTreasureQuest.registerMove(explorer);

        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsOnlyKeys(explorer.name());
        assertThat(historyTreasureQuest.historyMovementsPerExplorer()).containsValues(List.of(explorer, explorer));
    }

    @Test
    void register_move_for_an_unknown_explorer_is_forbidden(
            Explorer explorer,
            @ExplorerConfiguration(name = "Michel") Explorer explorer2
    ) {
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(buildQuest(List.of(explorer)));

        assertThatThrownBy(() ->
                historyTreasureQuest.registerMove(explorer2)
        ).isInstanceOf(ExplorerIsNotAllowedToDoThisQuestException.class);

    }

    private TreasureQuest buildQuest(List<Explorer> explorers) {
        return new TreasureQuest(
                new TreasureMap(new Dimension(4, 5), emptyList(), emptyList()),
                new Explorers(explorers)
        );
    }

}
