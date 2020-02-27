package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

        explorerOrchestrator.explorerNames().forEach(treasureQuest::executeMove);

        log.info("Final position {}", treasureQuest.historyMovements().get(treasureQuest.historyMovements().size() - 1));
        log.info("Quest is finished");
        return treasureQuest;
    }

}
