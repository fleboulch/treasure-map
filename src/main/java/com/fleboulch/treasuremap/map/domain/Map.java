package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.List;

public class Map {

    private final Dimension dimension;
    private final List<TreasureBox> treasureBoxes;

    public Map(Dimension dimension, List<TreasureBox> treasureBoxes) {
        this.dimension = Domain.validateNotNull(dimension, "A map should have a dimension");
        this.treasureBoxes = treasureBoxes;
    }

    public Dimension dimension() {
        return dimension;
    }

    public List<TreasureBox> treasures() {
        return treasureBoxes;
    }

}
