package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.Dimension;
import com.fleboulch.treasuremap.map.domain.PlainsBox;
import com.fleboulch.treasuremap.map.domain.TreasureBox;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class TreasureQuest {

    private final Logger log = LoggerFactory.getLogger(TreasureQuest.class);

    private TreasureMap treasureMap;
    private Explorers explorers;
    private Explorers historyMovements;

    public TreasureQuest(TreasureMap treasureMap, Explorers explorers) {
        this.treasureMap = Domain.validateNotNull(treasureMap, "Quest should have a not null treasure map");
        Domain.validateNotNull(explorers, "Quest should have not null explorers");
        validateStartingCoordinatesFor(explorers.explorers());
        this.explorers = explorers;
        this.historyMovements = explorers;
    }

    public void executeNextMove(Name explorerName) {
        Explorer currentExplorer = getLastState(explorerName);
        MovementType movementType = currentExplorer.nextMovement();
        switch (movementType) {
            case A:
                goForwardAction(currentExplorer);
                break;
            case D:
                turn(currentExplorer, MovementType.D);
                break;
            case G:
                turn(currentExplorer, MovementType.G);
                break;
            default:
                throw new IllegalArgumentException("Unknown movement type"); // should never occured
        }

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


    private void goForwardAction(Explorer currentExplorer) {
        Coordinates nextCoordinates = currentExplorer.checkNextPositionWhenGoForward();
        Optional<PlainsBox> nextBox = treasureMap.from(nextCoordinates);

        if (nextBox.isPresent()) {
            switch (nextBox.get().getBoxType()) {
                case MOUNTAIN:
                    log.info("{} is blocked by mountain in [{}]", currentExplorer, nextCoordinates);

                    break;
                case TREASURE:
                    log.info("{} will go forward and collect one treasure on [{}]", currentExplorer, nextCoordinates);
                    currentExplorer = currentExplorer.goForwardAndCollect();
                    treasureMap.removeOneTreasureOn((TreasureBox) nextBox.get());

                    break;
                case PLAINS:
                    log.info("{} will go forward on [{}]", currentExplorer, nextCoordinates);
                    currentExplorer = currentExplorer.goForward();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("The box type %s is not known", nextBox.get().getBoxType()));
            }
        } else {
            log.info("{} trying to go outside the map on [{}]", currentExplorer, nextCoordinates);

        }
        addToHistory(currentExplorer);

    }

    private void addToHistory(Explorer exp) {
        historyMovements = historyMovements.add(exp.popMovement());
    }

    // TODO: handle multiple explorers
    private Explorer getLastState(Name explorerName) {
        return historyMovements.explorers().get(historyMovements.explorers().size() - 1);
    }

    private void turn(Explorer currentExplorer, MovementType movementType) {
        Explorer explorerAfterTurn = currentExplorer.turn(movementType);

        log.info("{} turned to '{}'. New orientation is {}", currentExplorer, movementType, explorerAfterTurn.orientation().orientationType());
        addToHistory(explorerAfterTurn);
    }

    public TreasureMap treasureMap() {
        return treasureMap;
    }

    public Explorers explorers() {
        return explorers;
    }

    public Explorers historyMovements() {
        return historyMovements;
    }

    @Override
    public String toString() {
        return "TreasureQuest{" +
                "treasureMap= \n" + treasureMap +
                "explorers= \n" + explorers +
                '}';
    }
}
