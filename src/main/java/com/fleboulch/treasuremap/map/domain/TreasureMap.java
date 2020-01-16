package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;
import com.fleboulch.treasuremap.map.domain.exceptions.BoxIsOutOfMapException;
import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TreasureMap {

    public static final String BR_CHARACTER = "\n";
    private final Dimension dimension;
    private final List<MountainBox> mountainBoxes;
    private final List<TreasureBox> treasureBoxes;

    public TreasureMap(Dimension dimension, List<MountainBox> mountainBoxes, List<TreasureBox> treasureBoxes) {
        this.dimension = Domain.validateNotNull(dimension, "A map should have a dimension");

        Domain.validateNotNull(mountainBoxes, "Treasure map should have not null mountains");
        Domain.validateNotNull(treasureBoxes, "Treasure map should have not null treasures");

        checkValidBoxes(mountainBoxes, treasureBoxes);
        this.mountainBoxes = mountainBoxes;
        this.treasureBoxes = treasureBoxes;
    }

    // TODO: need to refacto this method
    public PlainsBox from(Coordinates coordinates) {
        if (coordinates.hasValidCoordinates(dimension)) {
            Optional<MountainBox> mountain = mountainBoxes.stream()
                    .filter(mountainBox -> Objects.equals(mountainBox.coordinates(), coordinates))
                    .findFirst();

            if (mountain.isPresent()) {
                return mountain.get();
            }

            Optional<TreasureBox> treasure = treasureBoxes.stream()
                    .filter(treasureBox -> Objects.equals(treasureBox.coordinates(), coordinates))
                    .findFirst();

            if (treasure.isPresent()) {
                return treasure.get();
            }

            return new PlainsBox(coordinates);

        } else {
            // return a custom exception instead of generic one
            throw new IllegalArgumentException(String.format("'%s' are out of map with dimensions %s", coordinates, dimension));
        }
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
