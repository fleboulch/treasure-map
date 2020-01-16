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

    private static final Coordinates ONE_ONE_COORDINATES = Coordinates.of(1, 1);
    private static final Coordinates ONE_TWO_COORDINATES = Coordinates.of(1, 2);
    private static final Coordinates ONE_ZERO_COORDINATES = Coordinates.of(1, 0);
    private static final Coordinates TWO_ONE_COORDINATES = Coordinates.of(2, 1);

    @Test
    void create_movement_sequence_for_an_explorer() {
        String rawMovements = "AADADAGGA";
        Explorer explorer = buildExplorer(rawMovements, OrientationType.S);

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
        Explorer explorer = buildExplorer(rawMovements, OrientationType.S);

        List<MovementType> createdMovements = explorer.movements().movementTypes();

        assertThat(createdMovements).isEmpty();
    }

    @Test
    void fail_to_create_null_movement_sequence_for_an_explorer() {
        String rawMovements = null;

        assertThatThrownBy(() ->
                buildExplorer(rawMovements, OrientationType.S)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void fail_to_create_movement_sequence_with_invalid_instructions_for_an_explorer() {
        String rawMovements = "ADGW";

        assertThatThrownBy(() ->
                buildExplorer(rawMovements, OrientationType.S)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void explorer_cannot_be_on_a_mountain() {
        Explorer explorer = buildExplorer("", OrientationType.S);
        TreasureMap treasureMap = buildMapWithOneMountain();
        assertThatThrownBy(() ->
                explorer.isOnMountain(treasureMap)
        ).isInstanceOf(InvalidCurrentPositionException.class);

    }

    @Test
    void check_explorer_is_not_on_a_mountain() {
        Explorer explorer = buildExplorer("", OrientationType.S);
        TreasureMap treasureMap = buildSimpleMap();
        boolean onMountain = explorer.isOnMountain(treasureMap);

        assertThat(onMountain).isFalse();
    }

    @Test
    void check_explorer_is_on_a_treasure() {
        Explorer explorer = buildExplorer("", OrientationType.S);
        TreasureMap treasureMap = buildMapWithOneTreasure();
        boolean onTreasure = explorer.isOnTreasure(treasureMap);

        assertThat(onTreasure).isTrue();
    }

    @Test
    void check_explorer_is_not_on_a_treasure() {
        Explorer explorer = buildExplorer("", OrientationType.S);
        TreasureMap treasureMap = buildSimpleMap();
        boolean onTreasure = explorer.isOnTreasure(treasureMap);

        assertThat(onTreasure).isFalse();
    }


    @Test
    void check_explorer_is_on_a_plains() {
        Explorer explorer = buildExplorer("", OrientationType.S);
        TreasureMap treasureMap = buildSimpleMap();
        boolean onPlains = explorer.isOnPlains(treasureMap);

        assertThat(onPlains).isTrue();
    }

    @Test
    void explorer_go_forward_south() {
        Explorer explorer = buildExplorer("A", OrientationType.S);
        Explorer explorerAfterAction = explorer.goForward();

        assertThat(explorerAfterAction.coordinates()).isEqualTo(ONE_TWO_COORDINATES);
    }

    @Test
    void explorer_go_forward_north() {
        Explorer explorer = buildExplorer("A", OrientationType.N);
        Explorer explorerAfterAction = explorer.goForward();

        assertThat(explorerAfterAction.coordinates()).isEqualTo(ONE_ZERO_COORDINATES);
    }

    @Test
    void explorer_go_forward_east() {
        Explorer explorer = buildExplorer("A", OrientationType.E);
        Explorer explorerAfterAction = explorer.goForward();

        assertThat(explorerAfterAction.coordinates()).isEqualTo(TWO_ONE_COORDINATES);
    }

    private Explorer buildExplorer(String rawMovements, OrientationType orientation) {
        return Explorer.of(
                new Name("Lara"),
                ONE_ONE_COORDINATES,
                new Orientation(orientation),
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
        return new MountainBox(ONE_ONE_COORDINATES);
    }

    private TreasureBox buildTreasure() {
        return new TreasureBox(ONE_ONE_COORDINATES, 1);
    }

}
