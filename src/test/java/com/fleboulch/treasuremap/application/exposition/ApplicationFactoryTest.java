package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.kernel.utils.ConverterUtils;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ApplicationFactoryTest {

    private static final String DIMENSION = "C - 3 - 4";

    // valid mountain and treasure coordinates
    private static final String MOUNTAIN_1 = "M - 1 - 2";
    private static final String MOUNTAIN_2 = "M - 0 - 2";
    private static final String TREASURE_1 = "T - 1 - 2 - 4";
    private static final String TREASURE_2 = "T - 2 - 3 - 1";

    // invalid mountain and treasure coordinates
    private static final String INVALID_MOUNTAIN_1 = "M - 3 - 4";
    private static final String INVALID_TREASURE_1 = "T - 3 - 4 - 3";

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
            INVALID_MOUNTAIN_1,
            INVALID_TREASURE_1
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
