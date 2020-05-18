package com.fleboulch.treasuremap.ddd.infra;

import com.fleboulch.treasuremap.ddd.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    @Mock
    private StudentInMemory inMemory;

    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepository(inMemory);
    }

    @Test
    void when_student_student_is_unknown_it_should_return_empty() {
        when(inMemory.findById(any())).thenReturn(Optional.empty());

        Optional<Student> student = repository.findById(UUID.randomUUID());

        assertThat(student).isEmpty();
    }

    @Test
    void when_student_student_exists_it_should_return_the_student() {
        UUID id = UUID.randomUUID();
        Student student1 = new Student(id, "John", "Doe", 10);
        when(inMemory.findById(any())).thenReturn(Optional.of(student1));

        Optional<Student> student = repository.findById(id);

        assertThat(student).isEqualTo(Optional.of(student1));
    }

}
