package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;

public class ExplorerIsNotAllowedToDoThisQuestException extends RuntimeException {

    public ExplorerIsNotAllowedToDoThisQuestException(Explorer explorer) {
        super(String.format("The explorer %s is not allowed to do this quest", explorer));
    }
}
