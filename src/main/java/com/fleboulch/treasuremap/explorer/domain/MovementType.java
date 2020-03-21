package com.fleboulch.treasuremap.explorer.domain;

public enum MovementType {
    A,
    G,
    D;

    public Position executeMovement(Position position) {
        switch (this) {
            case A:
                return position.goForward();
            case G:
                return new Position(position.orientation().leftTurn(), position.coordinates());
            case D:
                return new Position(position.orientation().rightTurn(), position.coordinates());
            default:
                throw new IllegalArgumentException("Unknown movement type");
        }
    }

}
