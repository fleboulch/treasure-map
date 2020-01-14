package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public class Treasure extends Box {

    private final int nbTreasures;

    public Treasure(HorizontalAxis x, VerticalAxis y, int nbTreasures) {
        super(x, y);
        this.nbTreasures = Domain.validatePositive(nbTreasures, "The number of treasures should be positive");
    }

    public int nbTreasures() {
        return nbTreasures;
    }
}
