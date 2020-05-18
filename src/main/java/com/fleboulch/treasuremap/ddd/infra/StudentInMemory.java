package com.fleboulch.treasuremap.ddd.infra;

import com.fleboulch.treasuremap.ddd.domain.Student;

import java.util.*;

public class StudentInMemory {

    private List<Student> students;

    public StudentInMemory() {
        this.students = new ArrayList<>();
    }

    public Optional<Student> findById(UUID id) {
        return students.stream()
                .filter(student -> Objects.equals(student.id(), id))
                .findAny();

    }
}
