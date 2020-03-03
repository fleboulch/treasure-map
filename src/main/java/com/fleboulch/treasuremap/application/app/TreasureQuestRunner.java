package com.fleboulch.treasuremap.application.app;

import com.fleboulch.treasuremap.application.domain.ExplorerOrchestrator;
import com.fleboulch.treasuremap.application.domain.TreasureQuest;
import com.fleboulch.treasuremap.explorer.domain.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TreasureQuestRunner {

    private final Logger log = LoggerFactory.getLogger(TreasureQuestRunner.class);

    private final ExplorerOrchestrator explorerOrchestrator;

    public TreasureQuestRunner(ExplorerOrchestrator explorerOrchestrator) {
        this.explorerOrchestrator = explorerOrchestrator;
    }

    public TreasureQuest start(TreasureQuest treasureQuest) {
        log.info("Quest is starting");
        log.info("{}", treasureQuest.treasureMap());
        log.info("Initial start for explorer 1: {}", treasureQuest.explorers().explorers().get(0));

        List<Name> explorerNames = explorerOrchestrator.buildExplorerNamesFrom(treasureQuest.explorers());

        explorerNames.forEach(treasureQuest::executeMove);

        log.info("Final position {}", treasureQuest.historyMovements().explorers().get(treasureQuest.historyMovements().explorers().size() - 1));
        log.info("Quest is finished");
        return treasureQuest;
    }

}
