package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;

import java.util.List;
import java.util.stream.Collectors;

public class TreasureMap {

    public static final String BR_CHARACTER = "\n";
    private final Dimension dimension;
    private final List<MountainBox> mountainBoxes;
    private final List<TreasureBox> treasureBoxes;

    public TreasureMap(Dimension dimension, List<MountainBox> mountainBoxes, List<TreasureBox> treasureBoxes) {
        this.dimension = Domain.validateNotNull(dimension, "A map should have a dimension");
        checkValidBoxes(mountainBoxes, treasureBoxes);
        this.mountainBoxes = Domain.validateNotNull(mountainBoxes, "A map should not have null mountains");
        this.treasureBoxes = Domain.validateNotNull(treasureBoxes, "A map should not have null treasures");
    }

    private void checkValidBoxes(List<MountainBox> mountainBoxes, List<TreasureBox> treasureBoxes) {
        mountainBoxes.forEach(this::checkValidBox);
        treasureBoxes.forEach(this::checkValidBox);
    }

    private void checkValidBox(PlainsBox box) {
        if (!box.isInside(dimension)) {
            throw new BoxIsOutOfMapException(box, dimension);
        }
    }

    public Dimension dimension() {
        return dimension;
    }

    public List<MountainBox> mountainBoxes() {
        return mountainBoxes;
    }

    public List<TreasureBox> treasureBoxes() {
        return treasureBoxes;
    }

    @Override
    public String toString() {
        String mountain = mountainBoxes.stream()
                .map(MountainBox::toString)
                .collect(Collectors.joining(BR_CHARACTER));

        String treasures = treasureBoxes.stream()
                .map(TreasureBox::toString)
                .collect(Collectors.joining(BR_CHARACTER));

        return String.format("C - %s - %s %n%s%n%s%n",
                dimension.width().value(),
                dimension.height().value(),
                mountain,
                treasures
        );
    }
}
