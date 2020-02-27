package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.Name;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExplorerOrchestrator {

    public List<Name> buildExplorerNamesFrom(Explorers explorers) {
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

}
