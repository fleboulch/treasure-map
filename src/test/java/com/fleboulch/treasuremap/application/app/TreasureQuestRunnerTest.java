package com.fleboulch.treasuremap.application.app;

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

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ExplorerResolver.class)
class TreasureQuestRunnerTest {

    private static final Dimension DIMENSION = new Dimension(new Width(3), new Height(4));
    private static final Coordinates ZERO_ONE_COORDINATES = Coordinates.of(0, 1);

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

    @Test
    void simple_quest_with_one_explorer_and_turn_right_movement(
            @ExplorerZeroZeroCoordinates @ExplorerTurnRight @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroZeroCoordinates @ExplorerWestOrientation Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
    }

    @Test
    void simple_quest_with_one_explorer_and_turn_left_movement(
            @ExplorerZeroZeroCoordinates @ExplorerTurnLeft @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroZeroCoordinates Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
    }

    @Test
    void an_explorer_should_be_blocked_by_a_mountain(
            @ExplorerZeroZeroCoordinates @ExplorerWithOneGoForward @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroZeroCoordinates @ExplorerSouthOrientation Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildQuestWithOneMountain(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
    }

    @Test
    void an_explorer_should_collect_one_treasure_on_treasure_box_and_box_should_be_deleted(
            @ExplorerZeroZeroCoordinates @ExplorerWithOneGoForward @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroOneCoordinates @ExplorerSouthOrientation @ExplorerWithOneTreasure Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildQuestWithOneTreasure(beginExplorer, 1);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        assertThat(finalQuest.treasureMap().treasureBoxes()).isEmpty();

    }

    @Test
    void an_explorer_should_collect_one_treasure_on_treasure_box(
            @ExplorerZeroZeroCoordinates @ExplorerWithOneGoForward @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroOneCoordinates @ExplorerSouthOrientation @ExplorerWithOneTreasure Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildQuestWithOneTreasure(beginExplorer, 2);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        assertThat(finalQuest.treasureMap().treasureBoxes()).containsExactly(new TreasureBox(ZERO_ONE_COORDINATES, 1));

    }

    @Test
    void simple_quest_with_one_explorer_and_two_go_forward_movement(
            @ExplorerZeroZeroCoordinates @ExplorerWithTwoGoForward @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroOneCoordinates @ExplorerWithOneGoForward @ExplorerSouthOrientation Explorer firstMoveExplorer,
            @ExplorerZeroTwoCoordinates @ExplorerSouthOrientation Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).containsExactly(beginExplorer, firstMoveExplorer, finalExplorer);

    }

    @Test
    void execute_example_test(
            @ExplorerOneOneCoordinates @ExplorerWithExampleSequenceMovements @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerZeroThreeCoordinates @ExplorerSouthOrientation @ExplorerWithThreeTreasures Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildComplexQuest(beginExplorer);
        HistoryTreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovementsPerExplorer().get(beginExplorer.name());

        assertThat(explorerMovements).hasSize(10);
        assertThat(explorerMovements).contains(beginExplorer, finalExplorer);

        TreasureMap finalMap = finalQuest.treasureMap();

        assertThat(finalMap.mountainBoxes()).containsExactly(
                new MountainBox(Coordinates.of(1, 0)),
                new MountainBox(Coordinates.of(2, 1))
        );
        assertThat(finalMap.treasureBoxes()).containsExactly(
                new TreasureBox(Coordinates.of(1, 3), 2)
        );

    }

    private TreasureQuest buildSimpleQuest(Explorer beginExplorer) {
        return new TreasureQuest(
                new TreasureMap(DIMENSION, emptyList(), emptyList()),
                new Explorers(List.of(
                        beginExplorer
                ))
        );
    }

    private TreasureQuest buildQuestWithOneMountain(Explorer beginExplorer) {
        MountainBox mountain1 = new MountainBox(ZERO_ONE_COORDINATES);
        return new TreasureQuest(
                new TreasureMap(DIMENSION, List.of(mountain1), emptyList()),
                new Explorers(List.of(
                        beginExplorer
                ))
        );
    }

    private TreasureQuest buildQuestWithOneTreasure(Explorer beginExplorer, int nbTreasures) {
        TreasureBox treasure1 = new TreasureBox(ZERO_ONE_COORDINATES, nbTreasures);
        return new TreasureQuest(
                new TreasureMap(DIMENSION, emptyList(), List.of(treasure1)),
                new Explorers(List.of(
                        beginExplorer
                ))
        );
    }

    private TreasureQuest buildComplexQuest(Explorer beginExplorer) {
        Coordinates oneZeroCoordinates = Coordinates.of(1, 0);
        Coordinates twoOneCoordinates = Coordinates.of(2, 1);
        Coordinates zeroThreeCoordinates = Coordinates.of(0, 3);
        Coordinates oneThreeCoordinates = Coordinates.of(1, 3);

        MountainBox mountain1 = new MountainBox(oneZeroCoordinates);
        MountainBox mountain2 = new MountainBox(twoOneCoordinates);

        TreasureBox treasure1 = new TreasureBox(zeroThreeCoordinates, 2);
        TreasureBox treasure2 = new TreasureBox(oneThreeCoordinates, 3);

        return new TreasureQuest(
                new TreasureMap(DIMENSION, List.of(mountain1, mountain2), List.of(treasure1, treasure2)),
                new Explorers(List.of(
                        beginExplorer
                ))
        );
    }

}
