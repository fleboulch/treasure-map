package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;

import java.util.List;

public class Explorers {

    private final List<Explorer> explorers;

    public Explorers(List<Explorer> explorers) {
        this.explorers = explorers;
    }

    public List<Explorer> explorers() {
        return explorers;
    }
}
