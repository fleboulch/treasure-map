package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.MountainBox;
import com.fleboulch.treasuremap.map.domain.TreasureBox;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.resolvers.ExplorerConfiguration;
import com.fleboulch.treasuremap.resolvers.ExplorerResolver;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
class ExplorerTest {

    private static final Coordinates ONE_ONE_COORDINATES = Coordinates.of(1, 1);
    public static final Dimension DIMENSION_FIVE_FIVE = new Dimension(5, 5);

    @Test
    void create_movement_sequence_for_an_explorer(
            @ExplorerConfiguration(
                    yCoordinates = 1,
                    orientationType = OrientationType.S,
                    isExampleSequenceMovements = true
            ) Explorer explorer

    ) {
        List<MovementType> createdMovements = explorer.movements().movementTypes();

        assertThat(createdMovements).hasSize(9);

        assertThat(createdMovements).contains(MovementType.A, Index.atIndex(0));
        assertThat(createdMovements).contains(MovementType.A, Index.atIndex(1));
        assertThat(createdMovements).contains(MovementType.D, Index.atIndex(2));
        assertThat(createdMovements).contains(MovementType.A, Index.atIndex(3));
        assertThat(createdMovements).contains(MovementType.D, Index.atIndex(4));
        assertThat(createdMovements).contains(MovementType.A, Index.atIndex(5));
        assertThat(createdMovements).contains(MovementType.G, Index.atIndex(6));
        assertThat(createdMovements).contains(MovementType.G, Index.atIndex(7));
        assertThat(createdMovements).contains(MovementType.A, Index.atIndex(8));
    }

    @Test
    void create_empty_movement_sequence_for_an_explorer(
            @ExplorerConfiguration(yCoordinates = 1, orientationType = OrientationType.S) Explorer explorer
    ) {

        List<MovementType> createdMovements = explorer.movements().movementTypes();

        assertThat(createdMovements).isEmpty();
    }

    @Test
    void fail_to_create_null_movement_sequence_for_an_explorer() {
        String rawMovements = null;

        assertThatThrownBy(() ->
                buildInvalidExplorer(rawMovements)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void fail_to_create_movement_sequence_with_invalid_instructions_for_an_explorer() {
        String rawMovements = "ADGW";

        assertThatThrownBy(() ->
                buildInvalidExplorer(rawMovements)
        ).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void explorer_go_forward_south(
            @ExplorerConfiguration(yCoordinates = 1, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(orientationType = OrientationType.S, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        beginExplorer.goForward();

        assertThat(beginExplorer).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_north(
            @ExplorerConfiguration(yCoordinates = 1, orientationType = OrientationType.N, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        beginExplorer.goForward();

        assertThat(beginExplorer).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_east(
            @ExplorerConfiguration(yCoordinates = 1, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 2, yCoordinates = 1, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        beginExplorer.goForward();

        assertThat(beginExplorer).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_west(
            @ExplorerConfiguration(yCoordinates = 0, orientationType = OrientationType.W, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.W, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        beginExplorer.goForward();

        assertThat(beginExplorer).isEqualTo(expectedEndExplorer);
    }

    @Test
    @Disabled
    void explorer_turn_D_from_north(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.D) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, movements = MovementType.D) Explorer expectedEndExplorer
    ) {
//        Explorer explorerAfterAction = beginExplorer.turn(MovementType.D);
//
//        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    @Test
    @Disabled
    void explorer_turn_L_from_north(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.G) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.W, movements = MovementType.G) Explorer expectedEndExplorer
    ) {
//        Explorer explorerAfterAction = beginExplorer.turn(MovementType.G);
//
//        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_should_not_go_outside_the_map(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        beginExplorer.goForward();

        assertThat(beginExplorer).isEqualTo(expectedEndExplorer);
    }

    private void buildInvalidExplorer(String rawMovements) {
        Explorer.of(
                new Name("Lara"),
                new Position(
                        new Orientation(OrientationType.S),
                        ONE_ONE_COORDINATES
                ),
                List.of(MovementType.valueOf(rawMovements))
        );
    }

    private TreasureMap buildSimpleMap() {
        return new TreasureMap(DIMENSION_FIVE_FIVE, emptyList(), emptyList());
    }

    private TreasureMap buildMapWithOneMountain() {
        List<MountainBox> mountains = List.of(buildMountain());
        return new TreasureMap(DIMENSION_FIVE_FIVE, mountains, emptyList());
    }

    private TreasureMap buildMapWithOneTreasure() {
        List<TreasureBox> treasures = List.of(buildTreasure());
        return new TreasureMap(DIMENSION_FIVE_FIVE, emptyList(), treasures);
    }

    private MountainBox buildMountain() {
        return new MountainBox(ONE_ONE_COORDINATES);
    }

    private TreasureBox buildTreasure() {
        return new TreasureBox(ONE_ONE_COORDINATES, 1);
    }

}
