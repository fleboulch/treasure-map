package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
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
                .forEach(explorer -> saveAction(historyTreasureQuest, explorer));

        return historyTreasureQuest;
    }

    private void saveAction(HistoryTreasureQuest historyTreasureQuest, Explorer explorer) {
        // if explorer movements is empty then skip
        if (explorer.canPerformMovements()) {
            Explorer explorerNext = doAction(explorer);
            historyTreasureQuest.registerMove(explorerNext);
        }
    }

    private Explorer doAction(Explorer currentExplorer) {
        MovementType movementType = currentExplorer.movements().movementTypes().get(0);
        switch (movementType) {
            case A:
                return currentExplorer.goForward();
            case D:
                return currentExplorer.turn(MovementType.D);
//            case G:
//                return Gturn(treasureQuest, currentExplorer);
            default:
                throw new IllegalArgumentException("Unknown movement type"); // should never occured
        }

    }

//    private TreasureQuest goForward(TreasureQuest treasureQuest, Explorer currentExplorer) {
//        getExplorerByIndex(treasureQuest, 0).goForward();
//        return treasureQuest;
//
//    }

//    private TreasureQuest Dturn(TreasureQuest treasureQuest, Explorer currentExplorer) {
//        return null;
//    }
//
//    private TreasureQuest Gturn(TreasureQuest treasureQuest, Explorer currentExplorer) {
//        return null;
//    }


}
