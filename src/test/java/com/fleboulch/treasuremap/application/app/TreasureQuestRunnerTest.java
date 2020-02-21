package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.MountainBox;
import com.fleboulch.treasuremap.map.domain.TreasureBox;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.resolvers.ExplorerConfiguration;
import com.fleboulch.treasuremap.resolvers.ExplorerResolver;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(ExplorerResolver.class)
class TreasureQuestRunnerTest {

    private static final Dimension DIMENSION = new Dimension(3, 4);
    private static final Coordinates ZERO_ONE_COORDINATES = Coordinates.of(0, 1);

    private TreasureQuestRunner runner;

    @BeforeEach
    void setup() {
        runner = new TreasureQuestRunner();
    }

    @Test
    void simple_quest_with_one_explorer(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S) Explorer beginExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> historyExplorers = finalQuest.historyMovements();

        assertThat(historyExplorers).containsExactly(beginExplorer);

    }

    @Test
    void simple_quest_with_one_explorer_and_go_forward_movement(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 1, orientationType = OrientationType.S) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        testExplorerEquals(explorerMovements.get(0), beginExplorer);
        testExplorerEquals(explorerMovements.get(1), finalExplorer);

    }

    private void testExplorerEquals(Explorer actual, Explorer expected) {
        assertAll(
                () -> assertThat(actual.name()).isEqualTo(expected.name()),
                () -> assertThat(actual.position()).isEqualTo(expected.position()),
                () -> assertThat(actual.movements()).isEqualTo(expected.movements()),
                () -> assertThat(actual.nbCollectedTreasures()).isEqualTo(expected.nbCollectedTreasures())
        );
    }

    @Test
    void simple_quest_with_one_explorer_and_turn_right_movement(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.D) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.W) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        testExplorerEquals(explorerMovements.get(0), beginExplorer);
        testExplorerEquals(explorerMovements.get(1), finalExplorer);
    }

    @Test
    void simple_quest_with_one_explorer_and_turn_left_movement(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.G) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        testExplorerEquals(explorerMovements.get(0), beginExplorer);
        testExplorerEquals(explorerMovements.get(1), finalExplorer);
    }

    @Test
    void an_explorer_should_be_blocked_by_a_mountain(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildQuestWithOneMountain(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        testExplorerEquals(explorerMovements.get(0), beginExplorer);
        testExplorerEquals(explorerMovements.get(1), finalExplorer);
    }

    @Test
    void an_explorer_should_collect_one_treasure_on_treasure_box_and_box_should_be_deleted(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 1, orientationType = OrientationType.S, nbTreasures = 1) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildQuestWithOneTreasure(beginExplorer, 1);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        assertThat(finalQuest.treasureMap().treasureBoxes()).isEmpty();

    }

    @Test
    void an_explorer_should_collect_one_treasure_on_treasure_box(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 1, orientationType = OrientationType.S, nbTreasures = 1) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildQuestWithOneTreasure(beginExplorer, 2);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);
        assertThat(finalQuest.treasureMap().treasureBoxes()).containsExactly(new TreasureBox(ZERO_ONE_COORDINATES, 1));

    }

    @Test
    void simple_quest_with_one_explorer_and_two_go_forward_movement(
            @ExplorerConfiguration(
                    xCoordinates = 0,
                    yCoordinates = 0,
                    orientationType = OrientationType.S,
                    movements = {MovementType.A, MovementType.A}
            ) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 1, orientationType = OrientationType.S, movements = MovementType.A) Explorer firstMoveExplorer,
            @ExplorerConfiguration(xCoordinates = 0, orientationType = OrientationType.S) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, firstMoveExplorer, finalExplorer);

    }

    @Test
    void simple_quest_with_one_explorer_and_one_go_forward_movement_trying_to_go_outside_the_map(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 3, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 3, orientationType = OrientationType.S) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, finalExplorer);

    }

    @Test
    void simple_quest_with_one_explorer_and_two_go_forward_movement_trying_to_go_outside_the_map(
            @ExplorerConfiguration(
                    xCoordinates = 0,
                    yCoordinates = 3,
                    orientationType = OrientationType.S,
                    movements = {MovementType.A, MovementType.A}
            ) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 3, orientationType = OrientationType.S, movements = MovementType.A) Explorer firstMoveExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 3, orientationType = OrientationType.S) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildSimpleQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

        assertThat(explorerMovements).containsExactly(beginExplorer, firstMoveExplorer, finalExplorer);

    }

    @Test
    void execute_example_test(
            @ExplorerConfiguration(
                    yCoordinates = 1,
                    orientationType = OrientationType.S,
                    isExampleSequenceMovements = true
            ) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 3, orientationType = OrientationType.S, nbTreasures = 3) Explorer finalExplorer
    ) {
        TreasureQuest inputTreasureQuest = buildComplexQuest(beginExplorer);
        TreasureQuest finalQuest = runner.start(inputTreasureQuest);

        List<Explorer> explorerMovements = finalQuest.historyMovements();

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
