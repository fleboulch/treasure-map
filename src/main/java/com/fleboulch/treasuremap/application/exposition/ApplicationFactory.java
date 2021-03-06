package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.InputType;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.exceptions.DimensionConfigurationNotDefinedException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputRowException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputTypeRowException;
import com.fleboulch.treasuremap.explorer.domain.*;
import com.fleboulch.treasuremap.kernel.utils.ConverterUtils;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ApplicationFactory {

    public static final String CARET_DELIMITER = " - ";
    private static final int EXPECTED_NUMBER_OF_DIMENSION_PROPERTIES = 3;
    private static final int EXPECTED_NUMBER_OF_TREASURE_PROPERTIES = 4;
    private static final int EXPECTED_NUMBER_OF_MOUNTAIN_PROPERTIES = 3;
    private static final int INCLUSIVE_NUMBER_OF_EXPLORER_PROPERTIES_MIN = 5;
    private static final int INCLUSIVE_NUMBER_OF_EXPLORER_PROPERTIES_MAX = 6;
    public static final String EMPTY_STRING = "";

    private ApplicationFactory() {
    }

    public static TreasureQuest toDomain(List<String> treasureQuestConfigurations) {
        Dimension dimension = buildDimension(treasureQuestConfigurations);

        List<PlainsBox> plainsBoxes = buildPlainsBoxes(treasureQuestConfigurations);
        List<MountainBox> mountains = buildMountainBoxesFrom(plainsBoxes);
        List<TreasureBox> treasures = buildTreasureBoxes(plainsBoxes);
        List<Explorer> explorers = buildExplorers(treasureQuestConfigurations);

        TreasureMap treasureMap = new TreasureMap(dimension, mountains, treasures);
        return new TreasureQuest(treasureMap, new Explorers(explorers));
    }

    private static List<Explorer> buildExplorers(List<String> treasureQuestConfigurations) {
        return treasureQuestConfigurations.stream()
                .filter(rowAsString -> rowAsString.startsWith(InputType.A.name()))
                .map(ApplicationFactory::splittedConfiguration)
                .map(ApplicationFactory::toExplorer)
                .collect(toList());
    }


    private static List<PlainsBox> buildPlainsBoxes(List<String> treasureQuestConfigurations) {
        return treasureQuestConfigurations.stream()
                .filter(rowAsString ->
                        !rowAsString.startsWith(InputType.C.name()) &&
                                !rowAsString.startsWith(InputType.A.name())
                )
                .map(ApplicationFactory::splittedConfiguration)
                .map(ApplicationFactory::toPlainsBox)
                .collect(toList());
    }

    private static Dimension buildDimension(List<String> treasureQuestConfigurations) {
        return treasureQuestConfigurations.stream()
                .filter(rowAsString -> rowAsString.startsWith(InputType.C.name()))
                .map(ApplicationFactory::splittedConfiguration)
                .map(ApplicationFactory::toDimension)
                .findFirst()
                .orElseThrow(DimensionConfigurationNotDefinedException::new);
    }

    private static List<TreasureBox> buildTreasureBoxes(List<PlainsBox> plainsBoxes) {
        return plainsBoxes.stream()
                .filter(box -> Objects.equals(box.getBoxType(), BoxType.TREASURE))
                .map(box -> (TreasureBox) box)
                .collect(toList());
    }

    private static List<MountainBox> buildMountainBoxesFrom(List<PlainsBox> plainsBoxes) {
        return plainsBoxes.stream()
                .filter(box -> Objects.equals(box.getBoxType(), BoxType.MOUNTAIN))
                .map(box -> (MountainBox) box)
                .collect(toList());
    }

    private static String[] splittedConfiguration(String row) {
        return row.split(CARET_DELIMITER);
    }

    private static PlainsBox toPlainsBox(String[] line) {
        InputType boxType = InputType.valueOf(line[0].trim());

        switch (boxType) {
            case M:
                return toMountain(line);
            case T:
                return toTreasure(line);
            default:
                throw new InvalidInputTypeRowException(line);
        }
    }

    private static MountainBox toMountain(String[] line) {

        validateLine(line, EXPECTED_NUMBER_OF_MOUNTAIN_PROPERTIES);
        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        return new MountainBox(Coordinates.of(x, y));
    }

    private static TreasureBox toTreasure(String[] line) {
        validateLine(line, EXPECTED_NUMBER_OF_TREASURE_PROPERTIES);

        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        int nbTreasures = ConverterUtils.toInt(line[3]);
        return new TreasureBox(Coordinates.of(x, y), nbTreasures);
    }


    private static Dimension toDimension(String[] line) {
        validateLine(line, EXPECTED_NUMBER_OF_DIMENSION_PROPERTIES);

        int width = ConverterUtils.toInt(line[1]);
        int height = ConverterUtils.toInt(line[2]);
        return new Dimension(width, height);
    }

    private static Explorer toExplorer(String[] line) {
        validateLineWithNumberOfPropertiesBetween(line, INCLUSIVE_NUMBER_OF_EXPLORER_PROPERTIES_MIN, INCLUSIVE_NUMBER_OF_EXPLORER_PROPERTIES_MAX);

        String name = line[1];
        int horizontalAxis = ConverterUtils.toInt(line[2]);
        int verticalAxis = ConverterUtils.toInt(line[3]);
        String orientation = line[4];

        return Explorer.of(
                new Name(name),
                new Position(
                        new Orientation(OrientationType.valueOf(orientation)),
                        Coordinates.of(horizontalAxis, verticalAxis)
                ),
                buildMovements(buildRawMovementsFrom(line))
        );

    }

    private static List<MovementType> buildMovements(String rawMovements) {
        String[] split = rawMovements.split(EMPTY_STRING);

        return Arrays.stream(split).map(ApplicationFactory::valueOf)
                .collect(toList());
    }

    private static MovementType valueOf(String it) {
        if (Objects.equals(it, EMPTY_STRING)) {
            return null;
        }
        return MovementType.valueOf(it);
    }

    private static String buildRawMovementsFrom(String[] line) {
        int numberOfProperties = line.length;

        // if no movements have been specified, return an empty string
        if (numberOfProperties == INCLUSIVE_NUMBER_OF_EXPLORER_PROPERTIES_MIN) {
            return "";
        }
        return line[5];

    }

    private static void validateLineWithNumberOfPropertiesBetween(String[] line, int inclusiveNumberOfPropertiesMin, int inclusiveNumberOfPropertiesMax) {
        int numberOfProperties = line.length;
        if (numberOfProperties < inclusiveNumberOfPropertiesMin && numberOfProperties > inclusiveNumberOfPropertiesMax) {
            throw new InvalidInputRowException(line);
        }
    }

    private static void validateLine(String[] line, int expectedNumberOfProperties) {
        if (line.length != expectedNumberOfProperties) {
            throw new InvalidInputRowException(line);
        }
    }

    public static List<String> toExposition(TreasureQuest treasureQuest) {
        List<String> expositionDimension = buildExpositionDimension(treasureQuest.treasureMap().dimension());
        List<String> expositionMountains = buildExpositionMountains(treasureQuest.treasureMap().mountainBoxes());
        List<String> expositionTreasures = buildExpositionTreasures(treasureQuest.treasureMap().treasureBoxes());

        List<String> expositionExplorers = buildExpositionExplorers(treasureQuest.historyMovements());

        return concatenate(expositionDimension, expositionMountains, expositionTreasures, expositionExplorers);
    }

    @SafeVarargs
    private static List<String> concatenate(List<String>... inputLists) {

        return Stream.of(inputLists)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    // TODO: working as there is only one explorer
    private static List<String> buildExpositionExplorers(Explorers historyMovementsPerExplorer) {
        Explorer exp = historyMovementsPerExplorer.explorers().get(historyMovementsPerExplorer.explorers().size() -1);
        return List.of(explorerToString(exp));
    }

    private static String explorerToString(Explorer explorer) {
        return String.format("A - %s - %s - %s - %s - %s",
                explorer.name().value(),
                explorer.coordinates().x().index(),
                explorer.coordinates().y().index(),
                explorer.orientation().orientationType(),
                explorer.nbCollectedTreasures()
        );
    }

    private static List<String> buildExpositionTreasures(List<TreasureBox> treasureBoxes) {
        return treasureBoxes.stream()
                .map(ApplicationFactory::buildExpositionTreasure)
                .collect(toList());
    }

    private static String buildExpositionTreasure(TreasureBox treasureBox) {
        return String.format("T - %s - %s - %s", treasureBox.coordinates().x().index(), treasureBox.coordinates().y().index(), treasureBox.nbTreasures());
    }

    private static List<String> buildExpositionMountains(List<MountainBox> mountainBoxes) {
        return mountainBoxes.stream()
                .map(ApplicationFactory::buildExpositionMountain)
                .collect(toList());
    }

    private static String buildExpositionMountain(MountainBox mountainBox) {
        return String.format("M - %s - %s", mountainBox.coordinates().x().index(), mountainBox.coordinates().y().index());
    }

    private static List<String> buildExpositionDimension(Dimension dimension) {
        return List.of(String.format("C - %s - %s", dimension.width(), dimension.height()));
    }
}
