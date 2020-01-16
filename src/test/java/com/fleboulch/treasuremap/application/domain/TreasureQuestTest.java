package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TreasureQuestTest {

    private static final Dimension DIMENSION = new Dimension(new Width(3), new Height(4));
    private static final Coordinates ZERO_ZERO_COORDINATES = Coordinates.of(0, 0);

    @ParameterizedTest
    @CsvSource(value = {
            "3,4",
            "0,8",
            "8,0",
            "8,9"
    })
    void it_should_fail_to_create_quest_with_out_of_map_coordinates_for_explorer(int x, int y) {
        Coordinates coordinates = Coordinates.of(x, y);
        assertThatThrownBy(() ->
                buildQuest(coordinates, false, OrientationType.S)
        ).isInstanceOf(ExplorerIsOutOfMapException.class);
    }

    @Test
    void it_should_succeed_to_create_quest_with_valid_coordinates_for_explorer() {
        TreasureQuest treasureQuest = buildQuest(ZERO_ZERO_COORDINATES, false, OrientationType.S);

        assertThat(treasureQuest.explorers().explorers()).hasSize(1);
    }

    @Test
    void impossible_for_an_explorer_to_begin_quest_on_a_mountain() {
        assertThatThrownBy(() ->
                buildQuest(ZERO_ZERO_COORDINATES, true, OrientationType.S)
        ).isInstanceOf(InvalidCurrentPositionException.class);

    }

    private TreasureQuest buildQuest(Coordinates explorerCoordinates, boolean startOnMountain, OrientationType orientation) {
        List<MountainBox> mountains = new ArrayList<>();
        if (startOnMountain) {
            mountains.add(new MountainBox(explorerCoordinates));
        }
        return new TreasureQuest(
                new TreasureMap(DIMENSION, mountains, emptyList()),
                new Explorers(List.of(
                        Explorer.of(
                                new Name("Laura"),
                                explorerCoordinates,
                                new Orientation(orientation),
                                ""
                        )
                ))
        );
    }

}
