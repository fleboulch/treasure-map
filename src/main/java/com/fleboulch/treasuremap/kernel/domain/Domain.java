package com.fleboulch.treasuremap.kernel.domain;

import com.fleboulch.treasuremap.kernel.exceptions.DomainException;
import com.fleboulch.treasuremap.kernel.exceptions.NegativeIndexException;

public class Domain {

    private Domain() {
    }

    public static <T> T validateNotNull(T attribute, String message) {
        if (attribute == null) {
            throw new DomainException(message);
        }
        return attribute;
    }

    public static int validatePositive(int attribute, String message) {
        if (attribute < 0) {
            throw new NegativeIndexException(message);
        }
        return attribute;
    }
}
