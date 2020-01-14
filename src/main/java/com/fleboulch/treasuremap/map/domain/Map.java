package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;

import java.util.List;

public class Map {

    private final Dimension dimension;
    private final List<TreasureBox> treasureBoxes;
    private final List<MountainBox> mountainBoxes;

    public Map(Dimension dimension, List<TreasureBox> treasureBoxes, List<MountainBox> mountainBoxes) {
        this.dimension = Domain.validateNotNull(dimension, "A map should have a dimension");
        checkValid(treasureBoxes, mountainBoxes);
        this.treasureBoxes = Domain.validateNotNull(treasureBoxes, "A map should not have null treasures");
        this.mountainBoxes = Domain.validateNotNull(mountainBoxes, "A map should not have null mountains");
    }

    private void checkValid(List<TreasureBox> treasureBoxes, List<MountainBox> mountainBoxes) {
        treasureBoxes.forEach(this::checkValidBox);
        mountainBoxes.forEach(this::checkValidBox);
    }

    private void checkValidBox(PlainsBox box) {
        if (!box.isInside(dimension)) {
            throw new BoxIsOutOfMapException();
        }
    }

    public Dimension dimension() {
        return dimension;
    }

    public List<TreasureBox> treasureBoxes() {
        return treasureBoxes;
    }

    public List<MountainBox> mountainBoxes() {
        return mountainBoxes;
    }
}
