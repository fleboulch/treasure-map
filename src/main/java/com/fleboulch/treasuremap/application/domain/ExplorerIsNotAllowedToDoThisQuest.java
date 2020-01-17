package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;

public class ExplorerIsNotAllowedToDoThisQuest extends RuntimeException {

    public ExplorerIsNotAllowedToDoThisQuest(Explorer explorer) {
        super(String.format("The explorer %s is not allowed to do this quest", explorer));
    }
}
