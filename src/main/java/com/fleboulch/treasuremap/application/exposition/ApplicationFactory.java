package com.fleboulch.treasuremap.application.exposition;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.application.exposition.exceptions.DimensionConfigurationNotDefinedException;
import com.fleboulch.treasuremap.kernel.utils.ConverterUtils;
import com.fleboulch.treasuremap.map.domain.*;

import java.util.List;
import java.util.stream.Collectors;

class ApplicationFactory {

    public static final String CARET_DELIMITER = " - ";

    private ApplicationFactory() {
    }

    public static TreasureQuest toDomain(List<String> treasureQuestConfigurations) {
        Dimension dimension = treasureQuestConfigurations.stream()
                .filter(rowAsString -> rowAsString.startsWith("C"))
                .map(ApplicationFactory::toDimension)
                .findFirst()
                .orElseThrow(DimensionConfigurationNotDefinedException::new);

        List<TreasureBox> treasures = treasureQuestConfigurations.stream()
                .filter(rowAsString -> rowAsString.startsWith("T"))
                .map(ApplicationFactory::toTreasure)
                .collect(Collectors.toList());

        List<MountainBox> mountains = treasureQuestConfigurations.stream()
                .filter(rowAsString -> rowAsString.startsWith("M"))
                .map(ApplicationFactory::toMountain)
                .collect(Collectors.toList());

        Map treasureMap = new Map(dimension, mountains, treasures);
        return new TreasureQuest(treasureMap);
    }

    private static MountainBox toMountain(String row) {
        String[] line = row.split(CARET_DELIMITER);// a CSV has comma separated lines

        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        return new MountainBox(new HorizontalAxis(x), new VerticalAxis(y));
    }

    private static TreasureBox toTreasure(String row) {
        String[] line = row.split(CARET_DELIMITER);// a CSV has comma separated lines

        int x = ConverterUtils.toInt(line[1]);
        int y = ConverterUtils.toInt(line[2]);
        int nbTreasures = ConverterUtils.toInt(line[3]);
        return new TreasureBox(new HorizontalAxis(x), new VerticalAxis(y), nbTreasures);
    }

    private static Dimension toDimension(String row) {
        String[] line = row.split(CARET_DELIMITER);// a CSV has comma separated lines

        int width = ConverterUtils.toInt(line[1]);
        int height = ConverterUtils.toInt(line[2]);
        return new Dimension(new Width(width), new Height(height));
    }
}
