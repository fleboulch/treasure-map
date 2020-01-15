package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.map.domain.Map;

public class TreasureQuest {

    private final Map treasureMap;

    public TreasureQuest(Map treasureMap) {
        this.treasureMap = treasureMap;
    }

    public Map treasureMap() {
        return treasureMap;
    }

    @Override
    public String toString() {
        return "TreasureQuest{" +
                "treasureMap= \n" + treasureMap +
                '}';
    }
}
