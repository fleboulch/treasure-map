package com.fleboulch.treasuremap.ddd.app;

import com.fleboulch.treasuremap.ddd.domain.Student;
import com.fleboulch.treasuremap.ddd.domain.Students;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCRUDTest {

    @Mock
    private Students students;

    private StudentCRUD service;

    @BeforeEach
    void setUp() {
        service = new StudentCRUD(students);
    }

    @Test
    void when_unknown_student_it_should_throw_an_exception() {
        when(students.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.findById(UUID.randomUUID())
        ).isInstanceOf(StudentDoesNotExistException.class);

    }

    @Test
    void when_student_student_exists_it_should_return_the_student() {
        UUID id = UUID.randomUUID();
        when(students.findById(any())).thenReturn(Optional.of(buildStudent(id)));

        Student student = service.findById(id);

        assertThat(student).isEqualTo(buildStudent(id));

    }

    private Student buildStudent(UUID id) {
        return new Student(id, "John", "Doe", 20);
    }
}
