package com.fleboulch.treasuremap.kernel.exceptions;

public class NegativeOrZeroAttributeException extends RuntimeException {

    public NegativeOrZeroAttributeException(String message) {
        super(message);
    }
}
