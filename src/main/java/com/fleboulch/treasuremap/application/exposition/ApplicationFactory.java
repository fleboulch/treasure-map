package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.exceptions.DimensionConfigurationNotDefinedException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputRowException;
import com.fleboulch.treasuremap.application.exposition.exceptions.InvalidInputTypeRowException;
import com.fleboulch.treasuremap.kernel.utils.ConverterUtils;
import com.fleboulch.treasuremap.map.domain.*;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class ApplicationFactory {

    public static final String CARET_DELIMITER = " - ";

    private ApplicationFactory() {
    }

    public static TreasureQuest toDomain(List<String> treasureQuestConfigurations) {
        Dimension dimension = treasureQuestConfigurations.stream()
                .filter(rowAsString -> rowAsString.startsWith("C"))
                .map(ApplicationFactory::splittedConfiguration)
                .map(ApplicationFactory::toDimension)
                .findFirst()
                .orElseThrow(DimensionConfigurationNotDefinedException::new);

        List<PlainsBox> plainsBoxes = treasureQuestConfigurations.stream()
                .filter(rowAsString -> !rowAsString.startsWith("C"))
                .map(ApplicationFactory::splittedConfiguration)
                .map(ApplicationFactory::toPlainsBox)
                .collect(toList());

        List<MountainBox> mountains = buildMountainBoxesFrom(plainsBoxes);

        List<TreasureBox> treasures = buildTreasureBoxes(plainsBoxes);

        Map treasureMap = new Map(dimension, mountains, treasures);
        return new TreasureQuest(treasureMap);
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
        String boxType = line[0].trim();
        if (Objects.equals(boxType, "M")) {
            return toMountain(line);
        } else if (Objects.equals(boxType, "T")) {
            return toTreasure(line);
        } else {
            throw new InvalidInputTypeRowException(line);
        }
    }

    private static MountainBox toMountain(String[] line) {

        validateLine(line, 3);
        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        return new MountainBox(new HorizontalAxis(x), new VerticalAxis(y));
    }

    private static TreasureBox toTreasure(String[] line) {
        validateLine(line, 4);

        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        int nbTreasures = ConverterUtils.toInt(line[3]);
        return new TreasureBox(new HorizontalAxis(x), new VerticalAxis(y), nbTreasures);
    }


    private static Dimension toDimension(String[] line) {
        validateLine(line, 3);

        int width = ConverterUtils.toInt(line[1]);
        int height = ConverterUtils.toInt(line[2]);
        return new Dimension(new Width(width), new Height(height));
    }

    private static void validateLine(String[] line, int expectedNumberOfProperties) {
        if (line.length != expectedNumberOfProperties) {
            throw new InvalidInputRowException(line);
        }
    }
}
