package com.fleboulch.treasuremap.application.exposition.exceptions;

public class InvalidInputTypeRowException extends RuntimeException {

    public InvalidInputTypeRowException(String[] row) {
        super(String.format("the row type '%s' is unknown", row.toString()));
    }
}
