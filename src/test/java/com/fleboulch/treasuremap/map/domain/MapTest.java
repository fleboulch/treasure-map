package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.exceptions.NegativeAttributeException;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MapTest {

    @Test
    void create_simple_map_with_valid_dimension() {
        int width = 2;
        int height = 3;
        Map validMap = buildSimpleMap(width, height);

        assertThat(validMap.dimension().width().value()).isEqualTo(width);
        assertThat(validMap.dimension().height().value()).isEqualTo(height);
        assertThat(validMap.mountainBoxes()).isEmpty();
        assertThat(validMap.treasureBoxes()).isEmpty();
    }

    @Test
    void create_a_map_with_one_treasure() {
        int width = 2;
        int height = 3;

        int x = 0;
        int y = 0;
        TreasureBox treasure = buildTreasure(x, y);
        Map validMap = buildMap(width, height, List.of(treasure));

        assertThat(validMap.dimension().width().value()).isEqualTo(width);
        assertThat(validMap.dimension().height().value()).isEqualTo(height);
        assertThat(validMap.mountainBoxes()).isEmpty();
        assertThat(validMap.treasureBoxes().size()).isOne();
    }

    @Test
    void create_a_map_with_one_invalid_treasure() {
        int width = 2;
        int height = 3;

        int x = 2;
        int y = 3;
        TreasureBox treasure = buildTreasure(x, y);

        assertThatThrownBy(
                () -> buildMap(width, height, List.of(treasure))
        ).isInstanceOf(BoxIsOutOfMapException.class);

    }

    private TreasureBox buildTreasure(int x, int y) {
        return new TreasureBox(new HorizontalAxis(x), new VerticalAxis(y), 1);
    }

    private Map buildSimpleMap(int width, int height) {
        return new Map(new Dimension(new Width(width), new Height(height)), emptyList(), emptyList());
    }

    private Map buildMap(int width, int height, List<TreasureBox> treasures) {
        return new Map(new Dimension(new Width(width), new Height(height)), treasures, emptyList());
    }
}
