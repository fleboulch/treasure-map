package com.fleboulch.treasuremap.ddd.domain;

import java.util.Optional;
import java.util.UUID;

public interface Students {

    Optional<Student> findById(UUID id);
}
