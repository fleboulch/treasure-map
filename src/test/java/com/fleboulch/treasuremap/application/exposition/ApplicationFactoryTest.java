package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.exceptions.DimensionConfigurationNotDefinedException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputRowException;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import com.fleboulch.treasuremap.resolvers.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
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
    void it_should_successfully_convert_configuration_without_mountain_and_treasure_to_treasure_quest(Explorer explorer) {
        String explorerInput = convertToInput(explorer);

        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, explorerInput));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().mountainBoxes()).isEmpty();
        assertThat(treasureQuest.treasureMap().treasureBoxes()).isEmpty();
    }

    @Test
    void it_should_successfully_convert_configuration_with_one_mountain_to_treasure_quest(@ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0) Explorer explorer) {
        String explorerInput = convertToInput(explorer);

        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, MOUNTAIN_1, explorerInput));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().mountainBoxes()).hasSize(1);
        assertThat(treasureQuest.treasureMap().mountainBoxes().get(0)).isEqualTo(buildMountain(1, 2));
        assertThat(treasureQuest.treasureMap().treasureBoxes()).isEmpty();
    }

    @Test
    void it_should_convert_configuration_with_one_treasure_to_treasure_quest(Explorer explorer) {
        String explorerInput = convertToInput(explorer);
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, TREASURE_1, explorerInput));
        TreasureQuest treasureQuest = ApplicationFactory.toDomain(validConfiguration);

        assertThat(treasureQuest.treasureMap().dimension()).isEqualTo(buildDimension());
        assertThat(treasureQuest.treasureMap().treasureBoxes()).hasSize(1);
        assertThat(treasureQuest.treasureMap().treasureBoxes().get(0)).isEqualTo(buildTreasure(1, 2, 4));
        assertThat(treasureQuest.treasureMap().mountainBoxes()).isEmpty();
    }

    private String convertToInput(Explorer explorer) {
        String rawMovements = buildRawMovements(explorer.movements().movementTypes());

        return String.format("A - %s - %s - %s - %s",
                explorer.name().value(),
                explorer.coordinates(),
                explorer.orientation().orientationType(),
                rawMovements
        );
    }

    private String buildRawMovements(List<MovementType> movementTypes) {
        return movementTypes.stream()
                .map(Enum::name)
                .collect(Collectors.joining(""));

    }

    @Test
    void it_should_successfully_convert_complex_configuration_to_treasure_quest(@ExplorerConfiguration(xCoordinates = 0, yCoordinates = 0) Explorer explorer) {
        String explorerInput = convertToInput(explorer);
        List<String> validConfiguration = buildConfiguration(List.of(DIMENSION, TREASURE_1, TREASURE_2, MOUNTAIN_2, MOUNTAIN_1, explorerInput));
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

    @Test
    void it_should_convert_history_treasure_quest_to_exposition(
            Explorer explorer
    ) {
        TreasureQuest historyTreasureQuest = buildTreasureQuest(explorer);

        List<String> response = ApplicationFactory.toExposition(historyTreasureQuest);

        String caret = ApplicationFactory.CARET_DELIMITER;
        assertThat(response).containsExactly(
                String.format("C%s3%s4", caret, caret),
                String.format("M%s1%s1", caret, caret),
                String.format("T%s2%s2%s1", caret, caret, caret),
                String.format("A%sLaura%s1%s2%sE%s0", caret, caret, caret, caret, caret)
        );
    }

    @Test
    void it_should_convert_history_treasure_quest_to_exposition_and_remove_empty_treasure_box(
            @ExplorerConfiguration(xCoordinates = 2, yCoordinates = 1, orientationType = OrientationType.S, nbTreasures = 3) Explorer explorer
    ) {
        TreasureQuest historyTreasureQuest = buildTreasureQuest(explorer);

        List<String> response = ApplicationFactory.toExposition(historyTreasureQuest);

        String caret = ApplicationFactory.CARET_DELIMITER;
        assertThat(response).containsExactly(
                String.format("C%s3%s4", caret, caret),
                String.format("M%s1%s1", caret, caret),
                String.format("T%s2%s2%s1", caret, caret, caret),
                String.format("A%sLaura%s2%s1%sS%s3", caret, caret, caret, caret, caret)
        );
    }

    private TreasureQuest buildTreasureQuest(Explorer explorer) {
        return new TreasureQuest(buildTreasureMap(), buildExplorers(explorer));
    }

    private Explorers buildExplorers(Explorer explorer) {
        return new Explorers(List.of(explorer));
    }

    private TreasureMap buildTreasureMap() {
        return new TreasureMap(buildDimension(), List.of(buildMountain(1, 1)), List.of(buildTreasure(2, 2, 1)));
    }

    private TreasureBox buildTreasure(int x, int y, int nbTreasures) {
        return new TreasureBox(Coordinates.of(x, y), nbTreasures);
    }

    private Dimension buildDimension() {
        return new Dimension(3, 4);
    }

    private MountainBox buildMountain(int x, int y) {
        return new MountainBox(Coordinates.of(x, y));
    }


    private List<String> buildConfiguration(List<String> configurations) {
        return configurations;
    }

}
