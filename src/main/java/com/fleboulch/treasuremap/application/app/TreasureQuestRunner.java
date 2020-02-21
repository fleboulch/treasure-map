package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TreasureQuestRunner {

    private final Logger log = LoggerFactory.getLogger(TreasureQuestRunner.class);

    public TreasureQuestRunner() {
    }

    public TreasureQuest start(TreasureQuest treasureQuest) {
        log.info("Quest is starting");
        log.info("{}", treasureQuest.treasureMap());
        log.info("Initial start for explorer 1: {}", treasureQuest.explorers().explorers().get(0));

        ExplorerOrchestrator explorerOrchestrator = new ExplorerOrchestrator(treasureQuest.explorers());

        List<TreasureQuest> quests = explorerOrchestrator.explorerNames().stream()
                .map(explorerName -> saveAction(explorerName, treasureQuest))
//                .reduce((l, r) -> r);
                .collect(Collectors.toList());

        TreasureQuest finalQuest = quests.get(quests.size() -1);

//        if (finalQuest.()) {
//            log.info("Final position {}", treasureQuest.explorers().explorers().get(0));
//            log.info("Quest is finished");
//
//            return treasureQuest;
//        }

        log.info("Final position {}", finalQuest.explorers().explorers().get(0));
        log.info("Quest is finished");
        return finalQuest;
    }

    private TreasureQuest saveAction(Name explorerName, TreasureQuest treasureQuest) {
        Explorer currentExplorer = treasureQuest.getLastState(explorerName);

        executeAction(currentExplorer, treasureQuest);
//        historyTreasureQuest.registerMove(treasureQuest1.explorers().explorers().get(0));
//        historyTreasureQuest.setTreasureMap(treasureQuest1.treasureMap());

//        if (explorerNext.hasCollectedANewTreasure(currentExplorer)) {
//            log.info("Remove one treasure on [{}]", explorerNext.coordinates());
//            historyTreasureQuest.removeOneTreasure(explorerNext.coordinates());
//        }

        return treasureQuest;
    }

    private void executeAction(Explorer currentExplorer, TreasureQuest treasureQuest) {
        MovementType movementType = currentExplorer.nextMovement();
        Explorer explorerAfterAction = null;
        switch (movementType) {
            case A:
                explorerAfterAction = treasureQuest.goForwardAction(currentExplorer);
                break;
            case D:
                currentExplorer.turn(MovementType.D);
                explorerAfterAction = currentExplorer;
                log.info("{} turned to the right. New orientation is {}", currentExplorer, explorerAfterAction.orientation().orientationType());
                break;
            case G:
                currentExplorer.turn(MovementType.G);
                explorerAfterAction = currentExplorer;
                log.info("{} turned to the left. New orientation is {}", currentExplorer, explorerAfterAction.orientation().orientationType());
                break;
            default:
                throw new IllegalArgumentException("Unknown movement type"); // should never occured
        }

        explorerAfterAction = explorerAfterAction.popMovement();
        treasureQuest.setHistoryMovements(treasureQuest.addToHistory(explorerAfterAction));
        treasureQuest.popMovementFor(explorerAfterAction);

    }

}
