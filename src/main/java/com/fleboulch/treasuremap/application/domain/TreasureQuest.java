package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.PlainsBox;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class TreasureQuest {

    private final Logger log = LoggerFactory.getLogger(TreasureQuest.class);

    private final TreasureMap treasureMap;
    private final Explorers explorers;

    public TreasureQuest(TreasureMap treasureMap, Explorers explorers) {
        this.treasureMap = Domain.validateNotNull(treasureMap, "Quest should have a not null treasure map");
        Domain.validateNotNull(explorers, "Quest should have not null explorers");
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
        treasureMap.explorerIsOnMountain(explorer);
    }


    public Explorer goForwardAction(Explorer currentExplorer) {
        Coordinates nextCoordinates = currentExplorer.checkNextPositionWhenGoForward();
        Optional<PlainsBox> nextBox = treasureMap.from(nextCoordinates);

        if (nextBox.isPresent()) {
            switch (nextBox.get().getBoxType()) {
                case MOUNTAIN:
                    log.info("{} is blocked by mountain in [{}]", currentExplorer, nextCoordinates);
                    return currentExplorer;
                case TREASURE:
                    log.info("{} will go forward and collect one treasure on [{}]", currentExplorer, nextCoordinates);
                    return currentExplorer.goForwardAndCollect();
                case PLAINS:
                    log.info("{} will go forward on [{}]", currentExplorer, nextCoordinates);
                    return currentExplorer.goForward();
                default:
                    throw new IllegalArgumentException(String.format("The box type %s is not known", nextBox.get().getBoxType()));
            }
        } else {
            log.info("{} trying to go outside the map on [{}]", currentExplorer, nextCoordinates);
            return currentExplorer;
        }
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
