package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.shared.coordinates.domain.Coordinates;

public class InvalidCurrentPositionException extends RuntimeException {

    public InvalidCurrentPositionException(String explorerName, Coordinates explorerCoordinates) {
        super(String.format("%s cannot be on box %s because it's a mountain", explorerName, explorerCoordinates));
    }
}
