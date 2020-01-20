package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.springframework.stereotype.Component;

@Component
public class TreasureQuestRunner {

    public TreasureQuestRunner() {
    }

    public HistoryTreasureQuest start(TreasureQuest treasureQuest) {
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(treasureQuest);

        ExplorerOrchestrator explorerOrchestrator = new ExplorerOrchestrator(treasureQuest.explorers());

        // TODO: replace this stream with collectors
        explorerOrchestrator.explorers().explorers()
                .forEach(explorer -> saveAction(historyTreasureQuest, explorer, treasureQuest.treasureMap()));

        return historyTreasureQuest;
    }

    private void saveAction(HistoryTreasureQuest historyTreasureQuest, Explorer explorer, TreasureMap treasureMap) {
        // if explorer movements is empty then skip
        if (explorer.canPerformMovements()) {
            Explorer explorerNext = doAction(explorer, treasureMap);
            historyTreasureQuest.registerMove(explorerNext);
        }
    }

    private Explorer doAction(Explorer currentExplorer, TreasureMap treasureMap) {
        MovementType movementType = currentExplorer.movements().movementTypes().get(0);
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
        Coordinates nextPosition = currentExplorer.checkNextPosition();
        if (treasureMap.containsMountainOn(nextPosition)) {
            return currentExplorer;
        }
        if (treasureMap.containsTreasureOn(nextPosition)) {
            return currentExplorer.goForwardAndCollect();
        }
        return currentExplorer.goForward();
    }

}
