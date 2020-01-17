package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.List;

public class ExplorerOrchestrator {

    private final Explorers explorers;

    public ExplorerOrchestrator(Explorers explorers) {
        Domain.validateNotNull(explorers, "Explorers should not be null");
        this.explorers = buildExplorers(explorers);
    }

    private Explorers buildExplorers(Explorers explorers) {
        return new Explorers(List.of(
                explorers.explorers().get(0)
        ));
    }

    public Explorers explorers() {
        return explorers;
    }
}
