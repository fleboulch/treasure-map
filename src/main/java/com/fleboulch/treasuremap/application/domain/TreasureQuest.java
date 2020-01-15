package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.map.domain.TreasureMap;

public class TreasureQuest {

    private final TreasureMap treasureMap;

    public TreasureQuest(TreasureMap treasureMap) {
        this.treasureMap = treasureMap;
    }

    public TreasureMap treasureMap() {
        return treasureMap;
    }

    @Override
    public String toString() {
        return "TreasureQuest{" +
                "treasureMap= \n" + treasureMap +
                '}';
    }
}
