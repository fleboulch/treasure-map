package com.fleboulch.treasuremap.ddd.app;

import java.util.UUID;

public class StudentDoesNotExistException extends RuntimeException {

    public StudentDoesNotExistException(UUID id) {
        super(String.format("The student '%s' does not exist", id));

    }
}
