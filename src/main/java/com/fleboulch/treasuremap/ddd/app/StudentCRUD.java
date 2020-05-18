package com.fleboulch.treasuremap.ddd.app;

import com.fleboulch.treasuremap.ddd.domain.Student;
import com.fleboulch.treasuremap.ddd.domain.Students;

import java.util.UUID;

public class StudentCRUD {

    private final Students students;

    public StudentCRUD(Students students) {
        this.students = students;
    }

    public Student findById(UUID id) {
        return students.findById(id).orElseThrow(
                () -> new StudentDoesNotExistException(id)
        );
    }
}
