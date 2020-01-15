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

    public static final Dimension DIMENSION = new Dimension(new Width(3), new Height(4));

    @ParameterizedTest
    @CsvSource(value = {
            "3,4",
            "0,8",
            "8,0",
            "8,9"
    })
    void it_should_fail_to_create_quest_with_out_of_map_coordinates_for_explorer(int x, int y) {
        assertThatThrownBy(() ->
                buildQuest(x, y, false)
        ).isInstanceOf(ExplorerIsOutOfMapException.class);
    }

    @Test
    void it_should_succeed_to_create_quest_with_valid_coordinates_for_explorer() {
        TreasureQuest treasureQuest = buildQuest(0, 0, false);

        assertThat(treasureQuest.explorers().explorers()).hasSize(1);
    }

    @Test
    void impossible_for_an_explorer_to_begin_quest_on_a_mountain() {
        assertThatThrownBy(() ->
                buildQuest(0, 0, true)
        ).isInstanceOf(InvalidCurrentPositionException.class);

    }

    private TreasureQuest buildQuest(int x, int y, boolean startOnMountain) {
        List<MountainBox> mountains = new ArrayList<>();
        if (startOnMountain) {
            mountains.add(new MountainBox(Coordinates.of(x, y)));
        }
        return new TreasureQuest(
                new TreasureMap(DIMENSION, mountains, emptyList()),
                new Explorers(List.of(
                        Explorer.of(
                                new Name("Laura"),
                                Coordinates.of(x, y),
                                new Orientation(OrientationType.E),
                                ""
                        )
                ))
        );
    }

}
