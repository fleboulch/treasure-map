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

//        explorerOrchestrator.explorers().explorers().stream()
//                .forEach(explorerAction -> {
//                    Explorer explorerNext = doAction(explorerAction.movements().movementTypes().get(0), treasureQuest, explorerAction, explorerOrchestrator);
//                    historyTreasureQuest.
//                }
//                })
//                .;
//
//        Explorer firstExplorer = getExplorerByIndex(treasureQuest, 0);// 1 explorer (need an orchestrator to handle multiple explorers)
//        firstExplorer.movements().movementTypes().forEach(movement -> doAction(movement, treasureQuest, firstExplorer));

        return historyTreasureQuest;
    }

    private Explorer getExplorerByIndex(TreasureQuest treasureQuest, int index) {
        return treasureQuest.explorers().explorers().get(index);
    }

    private Explorer doAction(MovementType movementType, TreasureQuest treasureQuest, Explorer currentExplorer, ExplorerOrchestrator explorerOrchestrator) {
        switch (movementType) {
            case A:
                return currentExplorer.goForward();
//            case D:
//                return Dturn(treasureQuest, currentExplorer);
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
