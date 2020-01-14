package com.fleboulch.treasuremap.map.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public class Height {

    private final int value;

    public Height(int value) {
        this.value = Domain.validatePositive(value, "A height should have a positive value");
    }

    public int value() {
        return value;
    }
}
