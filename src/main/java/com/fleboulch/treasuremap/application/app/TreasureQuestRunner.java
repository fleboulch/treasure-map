package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.HistoryTreasureQuest;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.map.domain.PlainsBox;
import com.fleboulch.treasuremap.map.domain.TreasureMap;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TreasureQuestRunner {

    private final Logger log = LoggerFactory.getLogger(TreasureQuestRunner.class);

    public TreasureQuestRunner() {
    }

    public HistoryTreasureQuest start(TreasureQuest treasureQuest) {
        log.info("Quest is starting");
        log.info("{}", treasureQuest.treasureMap());
        HistoryTreasureQuest historyTreasureQuest = HistoryTreasureQuest.of(treasureQuest);

        ExplorerOrchestrator explorerOrchestrator = new ExplorerOrchestrator(treasureQuest.explorers());

        Optional<HistoryTreasureQuest> finalQuest = explorerOrchestrator.explorerNames().stream()
                .map(explorerName -> saveAction(historyTreasureQuest, explorerName, treasureQuest))
                .reduce((l, r) -> r);

        log.info("Quest is finished");

        if (finalQuest.isEmpty()) {
            return historyTreasureQuest;
        }

        return finalQuest.get();
    }

    private HistoryTreasureQuest saveAction(HistoryTreasureQuest historyTreasureQuest, Name explorerName, TreasureQuest treasureQuest) {
        Explorer currentExplorer = historyTreasureQuest.getLastState(explorerName);

        Explorer explorerNext = executeAction(currentExplorer, treasureQuest);
        historyTreasureQuest.registerMove(explorerNext);

        if (explorerNext.hasCollectedANewTreasure(currentExplorer)) {
            log.info("Remove one treasure on [{}]", explorerNext.coordinates());
            historyTreasureQuest.removeOneTreasure(explorerNext.coordinates());
        }

        return historyTreasureQuest;
    }

    private Explorer executeAction(Explorer currentExplorer, TreasureQuest treasureQuest) {
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

        return explorerAfterAction.popMovement();

    }

}
