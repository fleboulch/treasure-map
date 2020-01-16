package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.TreasureMap;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TreasureQuest {

    private final TreasureMap treasureMap;
    private Explorers explorers;

    public TreasureQuest(TreasureMap treasureMap, Explorers explorers) {
        this.treasureMap = treasureMap;
        validateStartingCoordinatesFor(explorers.explorers());
        this.explorers = explorers;
    }

    private void validateStartingCoordinatesFor(List<Explorer> explorers) {
        explorers.forEach(this::validateStartingCoordinatesFor);
    }

    private void validateStartingCoordinatesFor(Explorer explorer) {
        Dimension dimension = treasureMap.dimension();
        if (!explorer.hasValidCoordinates(dimension)) {
            throw new ExplorerIsOutOfMapException(explorer, dimension);
        }
        explorer.isOnMountain(treasureMap);
    }

    public TreasureMap treasureMap() {
        return treasureMap;
    }

    public Explorers explorers() {
        return explorers;
    }

    @Override
    public String toString() {
        return "TreasureQuest{" +
                "treasureMap= \n" + treasureMap +
                "explorers= \n" + explorers +
                '}';
    }
}
