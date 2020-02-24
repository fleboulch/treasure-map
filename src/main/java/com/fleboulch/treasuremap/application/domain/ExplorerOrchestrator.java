package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import com.fleboulch.treasuremap.kernel.domain.Domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExplorerOrchestrator {

    private final List<Name> explorerNames;

    public ExplorerOrchestrator(Explorers explorers) {
        Domain.validateNotNull(explorers, "Explorers should not be null");
        this.explorerNames = buildExplorerNamesFrom(explorers);
    }

    private List<Name> buildExplorerNamesFrom(Explorers explorers) {
        if (explorers.explorers().size() > 1) {
            throw new MultipleExplorersQuestNotImplementedException();
        }

        return explorers.explorers().stream()
                .map(this::buildNamesForEachExplorer)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Name> buildNamesForEachExplorer(Explorer explorer) {
        return explorer.movements().movementTypes().stream()
                .map(movementType -> explorer.name())
                .collect(Collectors.toList());
    }

    public List<Name> explorerNames() {
        return explorerNames;
    }
}
