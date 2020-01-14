package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public class Width {

    private final int value;

    public Width(int value) {
        this.value = Domain.validatePositive(value, "A width should have a positive value");
    }

    public int value() {
        return value;
    }
}
