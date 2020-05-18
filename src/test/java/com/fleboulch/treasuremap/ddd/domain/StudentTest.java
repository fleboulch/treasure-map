package com.fleboulch.treasuremap.ddd.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudentTest {

    @Test
    void two_students_with_same_props_should_be_equal() {
        UUID id = UUID.randomUUID();
        Student student1 = new Student(id, "John", "Doe", 10);
        Student student2 = new Student(id, "John", "Doe", 10);

        assertThat(student1).isEqualTo(student2);
    }

    @Test
    void two_students_with_same_props_should_have_same_hashCode() {
        UUID id = UUID.randomUUID();
        Student student1 = new Student(id, "John", "Doe", 10);
        Student student2 = new Student(id, "John", "Doe", 10);

        assertThat(student1).hasSameHashCodeAs(student2);
    }

}
