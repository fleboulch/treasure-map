package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public class Dimension {

    private final Width width;
    private final Height height;

    public Dimension(Width width, Height height) {
        this.width = Domain.validateNotNull(width, "A dimension should have a width");
        this.height = Domain.validateNotNull(height, "A dimension should have a height");
    }

    public Width width() {
        return width;
    }

    public Height height() {
        return height;
    }
}
