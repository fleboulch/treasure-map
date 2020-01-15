package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.map.domain.HorizontalAxis;
import com.fleboulch.treasuremap.map.domain.VerticalAxis;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExplorerTest {

    @Test
    void create_movement_sequence_for_an_explorer() {
        String rawMovements = "AADADAGGA";
        Explorer explorer = buildExplorer(rawMovements);

        List<MovementType> createdMovements = explorer.movements().movementTypes();

        assertThat(createdMovements).hasSize(rawMovements.length());

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

    private Explorer buildExplorer(String rawMovements) {
        return Explorer.of(
                new Name("Lara"),
                new HorizontalAxis(1),
                new VerticalAxis(1),
                new Orientation(OrientationType.S),
                rawMovements
        );
    }

}
