package com.fleboulch.treasuremap.ddd.infra;

import com.fleboulch.treasuremap.ddd.domain.Student;
import com.fleboulch.treasuremap.ddd.domain.Students;

import java.util.Optional;
import java.util.UUID;

public class StudentRepository implements Students {

    private final StudentInMemory inMemory;

    public StudentRepository(StudentInMemory inMemory) {
        this.inMemory = inMemory;
    }

    @Override
    public Optional<Student> findById(UUID id) {
        return inMemory.findById(id);
    }
}
