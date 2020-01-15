package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExplorerTest {

    public static final Coordinates VALID_COORDINATES = Coordinates.of(1, 1);

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

    @Test
    void create_empty_movement_sequence_for_an_explorer() {
        String rawMovements = "";
        Explorer explorer = buildExplorer(rawMovements);

        List<MovementType> createdMovements = explorer.movements().movementTypes();

        assertThat(createdMovements).isEmpty();
    }

    @Test
    void fail_to_create_null_movement_sequence_for_an_explorer() {
        String rawMovements = null;

        assertThatThrownBy(() ->
                buildExplorer(rawMovements)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void fail_to_create_movement_sequence_with_invalid_instructions_for_an_explorer() {
        String rawMovements = "ADGW";

        assertThatThrownBy(() ->
                buildExplorer(rawMovements)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void explorer_cannot_be_on_a_mountain() {
        Explorer explorer = buildExplorer("");
        TreasureMap treasureMap = buildMapWithOneMountain();
        assertThatThrownBy(() ->
                explorer.isOnMountain(treasureMap)
        ).isInstanceOf(InvalidCurrentPositionException.class);

    }

    @Test
    void check_explorer_is_not_on_a_mountain() {
        Explorer explorer = buildExplorer("");
        TreasureMap treasureMap = buildSimpleMap();
        boolean onMountain = explorer.isOnMountain(treasureMap);

        assertThat(onMountain).isFalse();
    }

    @Test
    void check_explorer_is_on_a_treasure() {
        Explorer explorer = buildExplorer("");
        TreasureMap treasureMap = buildMapWithOneTreasure();
        boolean onTreasure = explorer.isOnTreasure(treasureMap);

        assertThat(onTreasure).isTrue();
    }

    @Test
    void check_explorer_is_not_on_a_treasure() {
        Explorer explorer = buildExplorer("");
        TreasureMap treasureMap = buildSimpleMap();
        boolean onTreasure = explorer.isOnTreasure(treasureMap);

        assertThat(onTreasure).isFalse();
    }


    @Test
    void check_explorer_is_on_a_plains() {
        Explorer explorer = buildExplorer("");
        TreasureMap treasureMap = buildSimpleMap();
        boolean onPlains = explorer.isOnPlains(treasureMap);

        assertThat(onPlains).isTrue();
    }


    private Explorer buildExplorer(String rawMovements) {
        return Explorer.of(
                new Name("Lara"),
                VALID_COORDINATES,
                new Orientation(OrientationType.S),
                rawMovements
        );
    }


    private TreasureMap buildSimpleMap() {
        return new TreasureMap(new Dimension(new Width(5), new Height(5)), emptyList(), emptyList());
    }

    private TreasureMap buildMapWithOneMountain() {
        List<MountainBox> mountains = List.of(buildMountain());
        return new TreasureMap(new Dimension(new Width(5), new Height(5)), mountains, emptyList());
    }

    private TreasureMap buildMapWithOneTreasure() {
        List<TreasureBox> treasures = List.of(buildTreasure());
        return new TreasureMap(new Dimension(new Width(5), new Height(5)), emptyList(), treasures);
    }

    private MountainBox buildMountain() {
        return new MountainBox(VALID_COORDINATES);
    }

    private TreasureBox buildTreasure() {
        return new TreasureBox(VALID_COORDINATES, 1);
    }

}
