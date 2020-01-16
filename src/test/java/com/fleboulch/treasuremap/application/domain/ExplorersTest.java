package com.fleboulch.treasuremap.application.domain;

import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExplorersTest {

    @Test
    void it_should_failed_to_create_empty_explorers() {
        assertThatThrownBy(() ->
                new Explorers(emptyList())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void it_should_failed_to_create_null_explorers() {
        assertThatThrownBy(() ->
                new Explorers(null)
        ).isInstanceOf(DomainException.class);
    }
}
