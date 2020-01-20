package com.fleboulch.treasuremap.explorer.domain;

public class ExplorerHasNoMoreMovementException extends RuntimeException {

    public ExplorerHasNoMoreMovementException(Explorer explorer) {
        super(String.format("%s has no more movement to execute", explorer));
    }
}
