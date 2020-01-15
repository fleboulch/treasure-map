package com.fleboulch.treasuremap.application.exposition.exceptions;

import static com.fleboulch.treasuremap.application.exposition.ApplicationFactory.CARET_DELIMITER;

public class InvalidInputTypeRowException extends RuntimeException {

    public InvalidInputTypeRowException(String[] row) {
        super(String.format("the row type '%s' is unknown", String.join(CARET_DELIMITER, row)));
    }
}
