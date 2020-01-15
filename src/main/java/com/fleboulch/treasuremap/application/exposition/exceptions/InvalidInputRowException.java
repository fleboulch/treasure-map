package com.fleboulch.treasuremap.application.exposition.exceptions;

public class InvalidInputRowException extends RuntimeException {

    public InvalidInputRowException(String[] row) {
        super(String.format("the row '%s' is invalid", row.toString()));
    }
}
