package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.ArrayList;
import java.util.List;

public class Explorers {

    private final List<Explorer> explorers;

    public Explorers(List<Explorer> explorers) {
        this.explorers = Domain.validateNotNull(explorers, "Explorers should have not null list");

        validateExplorers();
    }

    private void validateExplorers() {
        if (explorers.isEmpty()) {
            throw new IllegalArgumentException("Explorers should not be empty");
        }
    }

    public Explorers add(Explorer explorer) {
        List<Explorer> newExplorerList = new ArrayList<>(List.copyOf(explorers));
        newExplorerList.add(explorer);
        return new Explorers(newExplorerList);
    }

    public List<Explorer> explorers() {
        return explorers;
    }

}
