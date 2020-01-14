package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public class TreasureBox extends PlainsBox {

    private final int nbTreasures;

    public TreasureBox(HorizontalAxis x, VerticalAxis y, int nbTreasures) {
        super(x, y);
        this.nbTreasures = Domain.validatePositive(nbTreasures, "The number of treasures should be positive");
    }

    public int nbTreasures() {
        return nbTreasures;
    }
}
