package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.map.domain.TreasureMap;

public class ExplorerCannotBeginQuestOnMountainException extends RuntimeException {

    public ExplorerCannotBeginQuestOnMountainException(Explorer explorer, TreasureMap treasureMap) {
        super(String.format("%s cannot begin a quest on a mountain"));
    }
}
