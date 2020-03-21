package com.fleboulch.treasuremap.explorer.domain;

public class InvalidMovementTypeForTurnException extends RuntimeException {

    public InvalidMovementTypeForTurnException(MovementType direction) {
        super(String.format("'%s' is not a valid movement type for a turn", direction));
    }
}
