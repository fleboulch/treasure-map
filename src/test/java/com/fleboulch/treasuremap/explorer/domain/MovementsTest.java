package com.fleboulch.treasuremap.explorer.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class MovementsTest {

    @Test
    void pop_an_empty_movements_list_should_be_equal_to_input_movements() {
        Movements movements = buildMovements(emptyList());
        Movements actualMovements = movements.popMovement();

        assertThat(actualMovements).isEqualTo(movements);
    }

    @Test
    void pop_a_movement_list_with_one_item_should_be_equal_to_empty_movements_list() {
        Movements movements = buildMovements(List.of(MovementType.A));
        Movements actualMovements = movements.popMovement();

        assertThat(actualMovements).isEqualTo(new Movements(emptyList()));
    }

    @Test
    void pop_a_movement_list_with_multiple_items_should_only_remove_the_first_item() {
        MovementType secondMove = MovementType.D;
        Movements movements = buildMovements(List.of(MovementType.A, secondMove));
        Movements actualMovements = movements.popMovement();

        Movements expectedMovements = buildMovements(List.of(secondMove));

        assertThat(actualMovements).isEqualTo(expectedMovements);
    }

    private Movements buildMovements(List<MovementType> movementTypes) {
        return new Movements(movementTypes);
    }

}
