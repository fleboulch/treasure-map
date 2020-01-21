package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TreasureQuestRunner {

    public TreasureQuestRunner() {
    }

    public HistoryTreasureQuest start(TreasureQuest treasureQuest) {
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(treasureQuest);

        ExplorerOrchestrator explorerOrchestrator = new ExplorerOrchestrator(treasureQuest.explorers());

        Optional<HistoryTreasureQuest> finalQuest = explorerOrchestrator.explorerNames().stream()
                .map(explorerName -> saveAction(historyTreasureQuest, explorerName, treasureQuest.treasureMap()))
                .reduce((l, r) -> r);

        if (finalQuest.isEmpty()) {
            return historyTreasureQuest;
        }

        return finalQuest.get();
    }

    private HistoryTreasureQuest saveAction(HistoryTreasureQuest historyTreasureQuest, Name explorerName, TreasureMap treasureMap) {
        Explorer currentExplorer = historyTreasureQuest.getLastState(explorerName);

        Explorer explorerNext = executeAction(currentExplorer, treasureMap);
        historyTreasureQuest.registerMove(explorerNext);

        if (explorerNext.hasCollectedANewTreasure(currentExplorer)) {
            historyTreasureQuest.removeOneTreasure(explorerNext.coordinates());
        }

        return historyTreasureQuest;
    }

    private Explorer executeAction(Explorer currentExplorer, TreasureMap treasureMap) {
        MovementType movementType = currentExplorer.nextMovement();
        Explorer explorerAfterAction = null;
        switch (movementType) {
            case A:
                explorerAfterAction = goForwardAction(currentExplorer, treasureMap);
                break;
            case D:
                explorerAfterAction = currentExplorer.turn(MovementType.D);
                break;
            case G:
                explorerAfterAction = currentExplorer.turn(MovementType.G);
                break;
            default:
                throw new IllegalArgumentException("Unknown movement type"); // should never occured
        }

        return explorerAfterAction.popMovement();

    }

    private Explorer goForwardAction(Explorer currentExplorer, TreasureMap treasureMap) {
        Coordinates nextCoordinates = currentExplorer.checkNextPosition();
        if (treasureMap.containsMountainOn(nextCoordinates)) {
            return currentExplorer;
        }
        if (treasureMap.containsTreasureOn(nextCoordinates)) {
            return currentExplorer.goForwardAndCollect();
        }
        return currentExplorer.goForward();
    }

}
