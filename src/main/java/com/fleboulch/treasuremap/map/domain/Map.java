package com.fleboulch.treasuremap.map.domain;

import java.util.List;

public class Map {

    private final int width;
    private final int height;
    private final List<Treasure> treasures;

    public Map(int width, int height, List<Treasure> treasures) {
        this.width = width;
        this.height = height;
        this.treasures = treasures;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public List<Treasure> treasures() {
        return treasures;
    }

    @Override
    public String toString() {
        return "Map{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
