package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;

@Component
public class TreasureQuestRunner {

    public TreasureQuestRunner() {
    }

    public TreasureQuest start(TreasureQuest treasureQuest) {

        Explorer firstExplorer = getFirstExplorer(treasureQuest);// 1 explorer (need an orchestrator to handle multiple explorers)
        firstExplorer.movements().movementTypes().forEach(movement -> doAction(movement, treasureQuest, firstExplorer));

        return treasureQuest;
    }

    private Explorer getFirstExplorer(TreasureQuest treasureQuest) {
        return treasureQuest.explorers().explorers().get(0);
    }

    private TreasureQuest doAction(MovementType movementType, TreasureQuest treasureQuest, Explorer currentExplorer) {
        switch (movementType) {
            case A: return goForward(treasureQuest, currentExplorer);
            case D: return Dturn(treasureQuest, currentExplorer);
            case G: return Gturn(treasureQuest, currentExplorer);
            default: throw new IllegalArgumentException("Unknown movement type"); // should never occured
        }

    }

    private TreasureQuest goForward(TreasureQuest treasureQuest, Explorer currentExplorer) {

        switch (currentExplorer.orientation().orientationType()) {
            case S: return goForwardSouth(treasureQuest, currentExplorer);
            default: throw new RuntimeException("");
        }
    }

    private TreasureQuest goForwardSouth(TreasureQuest treasureQuest, Explorer currentExplorer) {
        return null;
    }

    private TreasureQuest Dturn(TreasureQuest treasureQuest, Explorer currentExplorer) {
        return null;
    }

    private TreasureQuest Gturn(TreasureQuest treasureQuest, Explorer currentExplorer) {
        return null;
    }



}
