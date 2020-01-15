package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.exceptions.DimensionConfigurationNotDefinedException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputRowException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputTypeRowException;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApplicationFactoryTest {

    private static final String DIMENSION = "C - 3 - 4";

    // valid mountain and treasure coordinates
    private static final String MOUNTAIN_1 = "M - 1 - 2";
    private static final String MOUNTAIN_2 = "M - 0 - 2";
    private static final String TREASURE_1 = "T - 1 - 2 - 4";
    private static final String TREASURE_2 = "T - 2 - 3 - 1";

    // invalid mountain and treasure coordinates
    private static final String OUT_OF_MAP_MOUNTAIN_1 = "M - 3 - 4";
    private static final String OUT_OF_MAP_TREASURE_1 = "T - 3 - 4 - 3";

    // invalid rows
    private static final String INCORRECT_TREASURE_1 = "T";
    private static final String INCORRECT_TREASURE_2 = "T - 3 - 4";
    private static final String INCORRECT_MOUNTAIN_1 = "M";
    private static final String INCORRECT_MOUNTAIN_2 = "M - 3";
    private static final String INCORRECT_DIMENSION_1 = "C";
    private static final String INCORRECT_DIMENSION_2 = "C - 1";

    // unknown row type
    private static final String UNKNOWN_ROW_TYPE_1 = "I - 3 - 4";
    private static final String UNKNOWN_ROW_TYPE_2 = "K - 3 - 4 - 1";

    @Test
    void it_should_successfully_convert_configuration_without_mountain_and_treasure_to_treasure_quest() {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().mountainBoxes()).isEmpty();
        assertThat(treasureQuest.treasureMap().treasureBoxes()).isEmpty();
    }

    @Test
    void it_should_successfully_convert_configuration_with_one_mountain_to_treasure_quest() {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, MOUNTAIN_1));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().mountainBoxes()).hasSize(1);
        assertThat(treasureQuest.treasureMap().mountainBoxes().get(0)).isEqualTo(buildMountain(1, 2));
        assertThat(treasureQuest.treasureMap().treasureBoxes()).isEmpty();
    }

    @Test
    void it_should_successfully_convert_configuration_with_one_treasure_to_treasure_quest() {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, TREASURE_1));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().treasureBoxes()).hasSize(1);
        assertThat(treasureQuest.treasureMap().treasureBoxes().get(0)).isEqualTo(buildTreasure(1, 2, 4));
        assertThat(treasureQuest.treasureMap().mountainBoxes()).isEmpty();
    }

    @Test
    void it_should_successfully_convert_complex_configuration_to_treasure_quest() {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, TREASURE_1, TREASURE_2, MOUNTAIN_2, MOUNTAIN_1));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().treasureBoxes()).hasSize(2);
        assertThat(treasureQuest.treasureMap().treasureBoxes().get(0)).isEqualTo(buildTreasure(1, 2, 4));
        assertThat(treasureQuest.treasureMap().treasureBoxes().get(1)).isEqualTo(buildTreasure(2, 3, 1));
        assertThat(treasureQuest.treasureMap().mountainBoxes()).hasSize(2);
        assertThat(treasureQuest.treasureMap().mountainBoxes().get(0)).isEqualTo(buildMountain(0, 2));
        assertThat(treasureQuest.treasureMap().mountainBoxes().get(1)).isEqualTo(buildMountain(1, 2));

    }

    @ParameterizedTest
    @ValueSource(strings = {
            INCORRECT_MOUNTAIN_1,
            INCORRECT_TREASURE_1,
            INCORRECT_MOUNTAIN_2,
            INCORRECT_TREASURE_2
    })
    void it_should_failed_to_convert_configuration_to_treasure_quest_when_mountain_or_treasure_is_invalid(String invalidConfig) {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, invalidConfig));
        assertThatThrownBy(() ->
                ApplicationFactory.toDomain(validConfiguration)
        ).isInstanceOf(InvalidInputRowException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            INCORRECT_DIMENSION_1,
            INCORRECT_DIMENSION_2
    })
    void it_should_failed_to_convert_configuration_to_treasure_quest_when_dimension_is_invalid(String invalidConfig) {
        List<String> validConfiguration = buildConfiguration(List.of(invalidConfig));
        assertThatThrownBy(() ->
                ApplicationFactory.toDomain(validConfiguration)
        ).isInstanceOf(InvalidInputRowException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            UNKNOWN_ROW_TYPE_1,
            UNKNOWN_ROW_TYPE_2,
    })
    void it_should_failed_to_convert_configuration_to_treasure_quest_when_row_type_is_invalid(String invalidConfig) {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, invalidConfig));
        assertThatThrownBy(() ->
                ApplicationFactory.toDomain(validConfiguration)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void it_should_failed_to_convert_configuration_to_treasure_quest_when_dimension_is_not_set() {
        List<String> validConfiguration = buildConfiguration(List.of(TREASURE_1));
        assertThatThrownBy(() ->
                ApplicationFactory.toDomain(validConfiguration)
        ).isInstanceOf(DimensionConfigurationNotDefinedException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            OUT_OF_MAP_MOUNTAIN_1,
            OUT_OF_MAP_TREASURE_1
    })
    void it_should_failed_to_convert_configuration_to_treasure_quest(String invalidConfig) {
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, invalidConfig));
        assertThatThrownBy(() ->
                ApplicationFactory.toDomain(validConfiguration)
        ).isInstanceOf(BoxIsOutOfMapException.class);
    }

    private TreasureBox buildTreasure(int x, int y, int nbTreasures) {
        return new TreasureBox(new HorizontalAxis(x), new VerticalAxis(y), nbTreasures);
    }

    private Dimension buildDimension() {
        return new Dimension(new Width(3), new Height(4));
    }

    private MountainBox buildMountain(int x, int y) {
        return new MountainBox(new HorizontalAxis(x), new VerticalAxis(y));
    }


    private List<String> buildConfiguration(List<String> configurations) {
        return configurations;
    }

}
