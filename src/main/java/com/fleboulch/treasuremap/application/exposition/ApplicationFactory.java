package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.Explorers;
import com.fleboulch.treasuremap.application.domain.InputType;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.exceptions.DimensionConfigurationNotDefinedException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputRowException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputTypeRowException;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.explorer.domain.Orientation;
import com.fleboulch.treasuremap.explorer.domain.OrientationType;
import com.fleboulch.treasuremap.kernel.utils.ConverterUtils;
import com.fleboulch.treasuremap.map.domain.*;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import com.fleboulch.treasuremap.shared.coordinates.domain.HorizontalAxis;
import com.fleboulch.treasuremap.shared.coordinates.domain.VerticalAxis;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ApplicationFactory {

    public static final String CARET_DELIMITER = " - ";

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
                .filter(box -> box instanceof TreasureBox)
                .map(box -> (TreasureBox) box)
                .collect(toList());
    }

    private static List<MountainBox> buildMountainBoxesFrom(List<PlainsBox> plainsBoxes) {
        return plainsBoxes.stream()
                .filter(box -> box instanceof MountainBox)
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

        validateLine(line, 3);
        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        return new MountainBox(Coordinates.of(x, y));
    }

    private static TreasureBox toTreasure(String[] line) {
        validateLine(line, 4);

        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        int nbTreasures = ConverterUtils.toInt(line[3]);
        return new TreasureBox(Coordinates.of(x, y), nbTreasures);
    }


    private static Dimension toDimension(String[] line) {
        validateLine(line, 3);

        int width = ConverterUtils.toInt(line[1]);
        int height = ConverterUtils.toInt(line[2]);
        return new Dimension(new Width(width), new Height(height));
    }

    private static Explorer toExplorer(String[] line) {
        validateLine(line, 6);

        String name = line[1];
        int horizontalAxis = ConverterUtils.toInt(line[2]);
        int verticalAxis = ConverterUtils.toInt(line[3]);
        String orientation = line[4];
        String rawMovements = line[5];

        return Explorer.of(
                new Name(name),
                Coordinates.of(horizontalAxis, verticalAxis),
                new Orientation(OrientationType.valueOf(orientation)),
                rawMovements
        );

    }

    private static void validateLine(String[] line, int expectedNumberOfProperties) {
        if (line.length != expectedNumberOfProperties) {
            throw new InvalidInputRowException(line);
        }
    }
}
