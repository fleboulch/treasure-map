package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.resolvers.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.assertj.core.data.Index;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
class ExplorerTest {

    private static final Coordinates ONE_ONE_COORDINATES = Coordinates.of(1, 1);

    @Test
    void create_movement_sequence_for_an_explorer(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation @ExplorerWithExampleSequenceMovements Explorer explorer

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
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {

        List<MovementType> createdMovements = explorer.movements().movementTypes();

        assertThat(createdMovements).isEmpty();
    }

    @Test
    void fail_to_create_null_movement_sequence_for_an_explorer() {
        String rawMovements = null;

        assertThatThrownBy(() ->
                buildInvalidExplorer(rawMovements)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void fail_to_create_movement_sequence_with_invalid_instructions_for_an_explorer() {
        String rawMovements = "ADGW";

        assertThatThrownBy(() ->
                buildInvalidExplorer(rawMovements)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void explorer_cannot_be_on_a_mountain(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        TreasureMap treasureMap = buildMapWithOneMountain();
        assertThatThrownBy(() ->
                explorer.isOnMountain(treasureMap)
        ).isInstanceOf(InvalidCurrentPositionException.class);

    }

    @Test
    void check_explorer_is_not_on_a_mountain(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        TreasureMap treasureMap = buildSimpleMap();
        boolean onMountain = explorer.isOnMountain(treasureMap);

        assertThat(onMountain).isFalse();
    }

    @Test
    void check_explorer_is_on_a_treasure(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        TreasureMap treasureMap = buildMapWithOneTreasure();
        boolean onTreasure = explorer.isOnTreasure(treasureMap);

        assertThat(onTreasure).isTrue();
    }

    @Test
    void check_explorer_is_not_on_a_treasure(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        TreasureMap treasureMap = buildSimpleMap();
        boolean onTreasure = explorer.isOnTreasure(treasureMap);

        assertThat(onTreasure).isFalse();
    }


    @Test
    void check_explorer_is_on_a_plains(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        TreasureMap treasureMap = buildSimpleMap();
        boolean onPlains = explorer.isOnPlains(treasureMap);

        assertThat(onPlains).isTrue();
    }

    @Test
    void explorer_go_forward_south(
            @ExplorerOneOneCoordinates @ExplorerWithOneGoForward @ExplorerSouthOrientation Explorer beginExplorer,
            @ExplorerOneTwoCoordinates @ExplorerWithOneGoForward @ExplorerSouthOrientation Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_north(
            @ExplorerOneOneCoordinates @ExplorerWithOneGoForward @ExplorerNorthOrientation Explorer beginExplorer,
            @ExplorerOneZeroCoordinates @ExplorerWithOneGoForward@ExplorerNorthOrientation Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_east(
            @ExplorerOneOneCoordinates @ExplorerWithOneGoForward Explorer beginExplorer,
            @ExplorerTwoOneCoordinates @ExplorerWithOneGoForward Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_go_forward_west(
            @ExplorerOneZeroCoordinates @ExplorerWithOneGoForward @ExplorerWestOrientation Explorer beginExplorer,
            @ExplorerZeroZeroCoordinates @ExplorerWithOneGoForward @ExplorerWestOrientation Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.goForward();

        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    @Test
    void explorer_turn_D_from_north(
            @ExplorerZeroZeroCoordinates @ExplorerTurnRight @ExplorerNorthOrientation Explorer beginExplorer,
            @ExplorerZeroZeroCoordinates @ExplorerTurnRight Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.turn(MovementType.D);

        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }
    @Test
    void explorer_turn_L_from_north(
            @ExplorerZeroZeroCoordinates @ExplorerTurnLeft @ExplorerNorthOrientation Explorer beginExplorer,
            @ExplorerZeroZeroCoordinates @ExplorerTurnLeft @ExplorerWestOrientation Explorer expectedEndExplorer
    ) {
        Explorer explorerAfterAction = beginExplorer.turn(MovementType.G);

        assertThat(explorerAfterAction).isEqualTo(expectedEndExplorer);
    }

    private void buildInvalidExplorer(String rawMovements) {
        Explorer.of(
                new Name("Lara"),
                ONE_ONE_COORDINATES,
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
        return new MountainBox(ONE_ONE_COORDINATES);
    }

    private TreasureBox buildTreasure() {
        return new TreasureBox(ONE_ONE_COORDINATES, 1);
    }

}
