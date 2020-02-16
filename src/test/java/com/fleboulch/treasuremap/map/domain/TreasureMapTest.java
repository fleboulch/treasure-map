package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.exceptions.InvalidCurrentPositionException;
import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import com.fleboulch.treasuremap.resolvers.ExplorerOneOneCoordinates;
import com.fleboulch.treasuremap.resolvers.ExplorerResolver;
import com.fleboulch.treasuremap.resolvers.ExplorerSouthOrientation;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
class TreasureMapTest {

    @Test
    void create_simple_map_with_valid_dimension() {
        int width = 2;
        int height = 3;
        TreasureMap validTreasureMap = buildSimpleMap(width, height);

        assertThat(validTreasureMap.dimension().width()).isEqualTo(width);
        assertThat(validTreasureMap.dimension().height()).isEqualTo(height);
        assertThat(validTreasureMap.mountainBoxes()).isEmpty();
        assertThat(validTreasureMap.treasureBoxes()).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0,1,1",
            "0,0,2,3"
    })
    void create_a_map_with_valid_treasure_coordinates(int x, int y, int width, int height) {
        TreasureBox treasure = buildTreasure(Coordinates.of(x, y));
        TreasureMap validTreasureMap = buildMapWithTreasures(width, height, List.of(treasure));

        assertThat(validTreasureMap.dimension().width()).isEqualTo(width);
        assertThat(validTreasureMap.dimension().height()).isEqualTo(height);
        assertThat(validTreasureMap.mountainBoxes()).isEmpty();
        assertThat(validTreasureMap.treasureBoxes().size()).isOne();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,1,1,1",
            "3,3,2,2",
            "0,8,2,2",
            "8,0,2,2",
    })
    void map_with_invalid_treasure_coordinates_should_not_be_created(int x, int y, int width, int height) {
        TreasureBox treasure = buildTreasure(Coordinates.of(x, y));

        assertThatThrownBy(
                () -> buildMapWithTreasures(width, height, List.of(treasure))
        ).isInstanceOf(BoxIsOutOfMapException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,1,1,1",
            "3,3,2,2"
    })
    void map_with_invalid_mountain_coordinates_should_not_be_created(int x, int y, int width, int height) {
        MountainBox mountainBox = buildMountain(Coordinates.of(x, y));

        assertThatThrownBy(
                () -> buildMapWithMountains(width, height, List.of(mountainBox))
        ).isInstanceOf(BoxIsOutOfMapException.class);
    }

    @Test
    void check_box_is_a_mountain_from_coordinates() {
        Coordinates mountainCoordinates = Coordinates.of(1, 2);
        TreasureMap treasureMap = buildMapWithMountains(3, 4, List.of(buildMountain(mountainCoordinates)));

        PlainsBox box = treasureMap.from(mountainCoordinates);
        assertThat(box).isInstanceOf(MountainBox.class);
        assertThat(box.coordinates()).isEqualTo(mountainCoordinates);

    }

    @Test
    void check_box_is_a_treasure_from_coordinates() {
        Coordinates treasureCoordinates = Coordinates.of(1, 2);
        TreasureMap treasureMap = buildMapWithTreasures(3, 4, List.of(buildTreasure(treasureCoordinates)));

        PlainsBox box = treasureMap.from(treasureCoordinates);
        assertThat(box).isInstanceOf(TreasureBox.class);
        assertThat(box.coordinates()).isEqualTo(treasureCoordinates);

    }

    @Test
    void check_box_is_a_plains_from_coordinates() {
        Coordinates coordinates = Coordinates.of(1, 2);
        TreasureMap treasureMap = buildSimpleMap(3, 4);

        PlainsBox box = treasureMap.from(coordinates);
        assertThat(box).isInstanceOf(PlainsBox.class);
        assertThat(box).isNotInstanceOf(MountainBox.class);
        assertThat(box).isNotInstanceOf(TreasureBox.class);
        assertThat(box.coordinates()).isEqualTo(coordinates);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,10",
            "10,0",
            "10,10",
    })
    void check_outside_coordinates_from_map_is_forbidden(int x, int y) {
        Coordinates coordinates = Coordinates.of(x, y);
        TreasureMap treasureMap = buildSimpleMap(3, 4);

        assertThatThrownBy(() ->
                treasureMap.from(coordinates)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void it_should_fail_to_create_treasure_map_with_null_dimension() {
        assertThatThrownBy(() ->
                new TreasureMap(null, emptyList(), emptyList())
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void it_should_fail_to_create_treasure_map_with_null_mountains() {
        assertThatThrownBy(() ->
                buildMapWithMountains(1,1, null)
        ).isInstanceOf(DomainException.class);
    }

    @Test
    void it_should_fail_to_create_treasure_map_with_null_treasures() {
        assertThatThrownBy(() ->
                buildMapWithTreasures(1,1, null)
        ).isInstanceOf(DomainException.class);
    }


    @Test
    void explorer_cannot_be_on_a_mountain(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        MountainBox mountain = buildMountain(explorer.coordinates());
        TreasureMap treasureMap = buildMapWithMountains(5, 5, List.of(mountain));
        assertThatThrownBy(() ->
                treasureMap.explorerIsOnMountain(explorer)
        ).isInstanceOf(InvalidCurrentPositionException.class);

    }

    @Test
    void check_explorer_is_not_on_a_mountain(
            @ExplorerOneOneCoordinates @ExplorerSouthOrientation Explorer explorer
    ) {
        TreasureMap treasureMap = buildSimpleMap(5, 5);
        boolean onMountain = treasureMap.explorerIsOnMountain(explorer);

        assertThat(onMountain).isFalse();
    }

    private MountainBox buildMountain(Coordinates coordinates) {
        return new MountainBox(coordinates);
    }

    private TreasureBox buildTreasure(Coordinates coordinates) {
        return new TreasureBox(coordinates, 1);
    }

    private TreasureMap buildSimpleMap(int width, int height) {
        return new TreasureMap(new Dimension(width, height), emptyList(), emptyList());
    }

    private TreasureMap buildMapWithTreasures(int width, int height, List<TreasureBox> treasures) {
        return new TreasureMap(new Dimension(width, height), emptyList(), treasures);
    }

    private TreasureMap buildMapWithMountains(int width, int height, List<MountainBox> mountains) {
        return new TreasureMap(new Dimension(width, height), mountains, emptyList());
    }
}
