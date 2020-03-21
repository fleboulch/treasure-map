package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.resolvers.ExplorerConfiguration;
import com.fleboulch.treasuremap.resolvers.ExplorerResolver;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
class ExplorerTest {

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
        Explorer explorerAfterAction = beginExplorer.goForward();

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_north(
            @ExplorerConfiguration(yCoordinates = 1, orientationType = OrientationType.N, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_east(
            @ExplorerConfiguration(yCoordinates = 1, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 2, yCoordinates = 1, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_west(
            @ExplorerConfiguration(yCoordinates = 0, orientationType = OrientationType.W, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.W, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void explorer_turn_D_from_north(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.D) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, movements = MovementType.D) Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.turn(MovementType.D);

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void explorer_turn_A_is_not_allowed(
            @ExplorerConfiguration Explorer beginExplorer
    ) {
        assertThatThrownBy(() ->
                beginExplorer.turn(MovementType.A)
        ).isInstanceOf(InvalidMovementTypeForTurnException.class);

    }

    @Test
    void explorer_turn_L_from_north(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.G) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.W, movements = MovementType.G) Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.turn(MovementType.G);

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void explorer_should_not_go_outside_the_map(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.N, movements = MovementType.A) Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        explorer_are_equals(explorerAfterAction, expectedEndExplorer);
    }

    @Test
    void when_explorer_pop_movement_it_should_remove_the_movement_correctly(
            @ExplorerConfiguration(movements = MovementType.A) Explorer beginExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.popMovement();
        assertThat(explorerAfterAction.movements().movementTypes()).isEmpty();
    }

    @Test
    void it_should_return_next_position_when_explorer_try_to_go_forward(
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0, orientationType = OrientationType.S, movements = MovementType.A) Explorer beginExplorer,
            @ExplorerConfiguration(xCoordinates = 0, yCoordinates = 1, orientationType = OrientationType.S) Explorer finalExplorer
    ) {
        Coordinates nextCoordinates = beginExplorer.checkNextPositionWhenGoForward();
        assertThat(nextCoordinates).isEqualTo(finalExplorer.coordinates());
    }

    void explorer_are_equals(Explorer actualExplorer, Explorer expectedExplorer) {
        assertThat(actualExplorer).isEqualTo(expectedExplorer);
        assertThat(actualExplorer.position()).isEqualTo(expectedExplorer.position());
        assertThat(actualExplorer.movements()).isEqualTo(expectedExplorer.movements());
        assertThat(actualExplorer.nbCollectedTreasures()).isEqualTo(expectedExplorer.nbCollectedTreasures());
    }

    private void buildInvalidExplorer(String rawMovements) {
        Explorer.of(
                new Name("Lara"),
                new Position(
                        new Orientation(OrientationType.S),
                        Coordinates.of(1, 1)
                ),
                List.of(MovementType.valueOf(rawMovements))
        );
    }

}
