package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.List;

public class Map {

    private final Dimension dimension;
    private final List<Treasure> treasures;

    public Map(Dimension dimension, List<Treasure> treasures) {
        this.dimension = Domain.validateNotNull(dimension, "A map should have a dimension");
        this.treasures = treasures;
    }

    public Dimension dimension() {
        return dimension;
    }

    public List<Treasure> treasures() {
        return treasures;
    }

}
