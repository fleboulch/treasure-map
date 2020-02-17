package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.explorer.domain.MovementType;
import com.fleboulch.treasuremap.resolvers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(ExplorerResolver.class)
class ExplorerOrchestratorTest {

    @Test
    void should_create_orchestrator_from_one_explorer_with_no_movement(Explorer explorer) {
        ExplorerOrchestrator orchestrator = new ExplorerOrchestrator(buildExplorersWith(explorer));

        assertThat(orchestrator.explorerNames()).isEmpty();
    }

    @Test
    void should_create_orchestrator_from_one_explorer_with_one_go_forward_movement(
            @ExplorerConfiguration(movements = MovementType.A) Explorer explorer
    ) {
        ExplorerOrchestrator orchestrator = new ExplorerOrchestrator(buildExplorersWith(explorer));

        assertThat(orchestrator.explorerNames()).containsExactly(explorer.name());
    }

    @Test
    void should_create_orchestrator_from_one_explorer_with_two_go_forward_movements(
            @ExplorerConfiguration(movements = {MovementType.A, MovementType.A}) Explorer beginExplorer,
            @ExplorerConfiguration(movements = MovementType.A) Explorer finalExplorer
    ) {
        ExplorerOrchestrator orchestrator = new ExplorerOrchestrator(buildExplorersWith(beginExplorer));

        assertThat(orchestrator.explorerNames()).containsExactly(finalExplorer.name(), finalExplorer.name());
    }

    @Test
    void orchestrator_with_multiple_explorers_has_not_been_implemented_yet(
            Explorer explorer1,
            @ExplorerConfiguration(name = "Michel") Explorer explorer2
    ) {
        assertThatThrownBy(() ->
                new ExplorerOrchestrator(new Explorers(List.of(explorer1, explorer2)))
        ).isInstanceOf(MultipleExplorersQuestNotImplementedException.class);

    }

    private Explorers buildExplorersWith(Explorer lauraExplorer) {
        return new Explorers(List.of(lauraExplorer));
    }

}
