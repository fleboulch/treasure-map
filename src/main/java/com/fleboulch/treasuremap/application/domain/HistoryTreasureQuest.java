package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.map.domain.TreasureMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HistoryTreasureQuest {

    private final TreasureMap treasureMap;
    private Map<Name, List<Explorer>> historyMovementsPerExplorer = new HashMap<>();

    private HistoryTreasureQuest(TreasureMap treasureMap, List<Explorer> explorers) {
        this.treasureMap = treasureMap;
        this.historyMovementsPerExplorer = buildMapPerExplorer(explorers);
    }

    private Map<Name, List<Explorer>> buildMapPerExplorer(List<Explorer> explorers) {
        return explorers.stream()
                .collect(Collectors.toMap(
                        Explorer::name,
                        List::of
                        )
                );
    }

    public static HistoryTreasureQuest of(TreasureQuest treasureQuest) {
        return new HistoryTreasureQuest(treasureQuest.treasureMap(), treasureQuest.explorers().explorers());
    }

    public TreasureMap treasureMap() {
        return treasureMap;
    }

    public Map<Name, List<Explorer>> historyMovementsPerExplorer() {
        return historyMovementsPerExplorer;
    }
}
