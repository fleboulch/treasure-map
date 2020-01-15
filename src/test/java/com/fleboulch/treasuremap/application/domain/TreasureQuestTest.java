package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.explorer.domain.Orientation;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;
import com.fleboulch.treasuremap.map.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static java.util.Collections.emptyList;
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
                buildQuest(x, y)
        ).isInstanceOf(ExplorerIsOutOfMapException.class);
    }

    private TreasureQuest buildQuest(int x, int y) {
        return new TreasureQuest(
                new TreasureMap(DIMENSION, emptyList(), emptyList()),
                new Explorers(List.of(
                        Explorer.of(
                                new Name("Laura"),
                                new HorizontalAxis(x),
                                new VerticalAxis(y),
                                new Orientation(OrientationType.E),
                                ""
                        )
                ))
        );
    }

}
