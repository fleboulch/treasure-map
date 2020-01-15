package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.map.domain.TreasureMap;

public class TreasureQuest {

    private final TreasureMap treasureMap;
    private final Explorers explorers;

    public TreasureQuest(TreasureMap treasureMap, Explorers explorers) {
        this.treasureMap = treasureMap;
        this.explorers = explorers;
    }

    public TreasureMap treasureMap() {
        return treasureMap;
    }

    public Explorers explorers() {
        return explorers;
    }

    @Override
    public String toString() {
        return "TreasureQuest{" +
                "treasureMap= \n" + treasureMap +
                "explorers= \n" + explorers +
                '}';
    }
}
