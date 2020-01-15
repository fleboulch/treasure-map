package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapTest {

    @Test
    void create_simple_map_with_valid_dimension() {
        int width = 2;
        int height = 3;
        TreasureMap validTreasureMap = buildSimpleMap(width, height);

        assertThat(validTreasureMap.dimension().width().value()).isEqualTo(width);
        assertThat(validTreasureMap.dimension().height().value()).isEqualTo(height);
        assertThat(validTreasureMap.mountainBoxes()).isEmpty();
        assertThat(validTreasureMap.treasureBoxes()).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0,0,1,1",
            "0,0,2,3"
    })
    void create_a_map_with_valid_treasure_coordinates(int x, int y, int width, int height) {
        TreasureBox treasure = buildTreasure(x, y);
        TreasureMap validTreasureMap = buildMapWithTreasures(width, height, List.of(treasure));

        assertThat(validTreasureMap.dimension().width().value()).isEqualTo(width);
        assertThat(validTreasureMap.dimension().height().value()).isEqualTo(height);
        assertThat(validTreasureMap.mountainBoxes()).isEmpty();
        assertThat(validTreasureMap.treasureBoxes().size()).isOne();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1,1,1,1",
            "3,3,2,2"
    })
    void map_with_invalid_treasure_coordinates_should_not_be_created(int x, int y, int width, int height) {
        TreasureBox treasure = buildTreasure(x, y);

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
        MountainBox mountainBox = buildMountain(x, y);

        assertThatThrownBy(
                () -> buildMapWithMountains(width, height, List.of(mountainBox))
        ).isInstanceOf(BoxIsOutOfMapException.class);
    }

    private MountainBox buildMountain(int x, int y) {
        return new MountainBox(new HorizontalAxis(x), new VerticalAxis(y));
    }

    private TreasureBox buildTreasure(int x, int y) {
        return new TreasureBox(new HorizontalAxis(x), new VerticalAxis(y), 1);
    }

    private TreasureMap buildSimpleMap(int width, int height) {
        return new TreasureMap(new Dimension(new Width(width), new Height(height)), emptyList(), emptyList());
    }

    private TreasureMap buildMapWithTreasures(int width, int height, List<TreasureBox> treasures) {
        return new TreasureMap(new Dimension(new Width(width), new Height(height)), emptyList(), treasures);
    }

    private TreasureMap buildMapWithMountains(int width, int height, List<MountainBox> mountains) {
        return new TreasureMap(new Dimension(new Width(width), new Height(height)), mountains, emptyList());
    }
}
