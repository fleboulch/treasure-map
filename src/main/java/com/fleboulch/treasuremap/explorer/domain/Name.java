package com.fleboulch.treasuremap.explorer.domain;

import com.fleboulch.treasuremap.kernel.domain.Domain;

public class Name {

    private final String value;

    public Name(String value) {
        this.value = Domain.validateNotNull(value, "value should not be null");
    }

    public String value() {
        return value;
    }
}
