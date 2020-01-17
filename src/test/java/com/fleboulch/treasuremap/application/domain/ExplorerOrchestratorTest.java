package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.explorer.domain.Explorer;
import com.fleboulch.treasuremap.resolvers.ExplorerResolver;
import com.fleboulch.treasuremap.resolvers.ExplorerWithOneGoForward;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ExplorerResolver.class)
class ExplorerOrchestratorTest {

    @Test
    void should_create_orchestrator_from_one_explorer_with_no_movement(Explorer explorer) {
        ExplorerOrchestrator orchestrator = new ExplorerOrchestrator(buildExplorersWith(explorer));

        assertThat(orchestrator.explorers().explorers()).containsExactly(explorer);
    }

    @Test
    void should_create_orchestrator_from_one_explorer_with_one_go_forward_movement(@ExplorerWithOneGoForward Explorer explorer) {
        ExplorerOrchestrator orchestrator = new ExplorerOrchestrator(buildExplorersWith(explorer));

        assertThat(orchestrator.explorers().explorers()).containsExactly(explorer);
    }

    private Explorers buildExplorersWith(Explorer lauraExplorer) {
        return new Explorers(List.of(lauraExplorer));
    }

}
