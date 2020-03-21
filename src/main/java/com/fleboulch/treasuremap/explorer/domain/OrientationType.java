package com.fleboulch.treasuremap.explorer.domain;

import java.util.HashMap;
import java.util.Map;

public enum OrientationType {
    N(0),
    S(180),
    E(90),
    W(270);

    private static final int QUARTER_TURN = 90;
    private static final int COMPLETE_TURN = 360;
    private static final Map<Integer, OrientationType> lookupMap = new HashMap<>();

    private final int angle;

    static {
        for (OrientationType orientationType: OrientationType.values()) {
            lookupMap.put(orientationType.angle, orientationType);
        }
    }

    OrientationType(int angle) {
        this.angle = angle;
    }

    public OrientationType turnRight() {
        int angleAfterTurn = (this.angle + QUARTER_TURN) % COMPLETE_TURN;
        return lookupMap.get(angleAfterTurn);
    }

    public OrientationType turnLeft() {
        int angleAfterTurn = (COMPLETE_TURN + this.angle - QUARTER_TURN) % COMPLETE_TURN;
        return lookupMap.get(angleAfterTurn);
    }
}
