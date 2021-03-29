package com.fleboulch.treasuremap.ddd.exposition;

import com.fleboulch.treasuremap.ddd.app.StudentCRUD;
import com.fleboulch.treasuremap.ddd.domain.Student;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentApi {

    private final StudentCRUD service;

    public StudentApi(StudentCRUD service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UUID> findById(@PathVariable UUID id) {
        Student student = service.findById(id);

        return ResponseEntity.ok(student.id());
    }
}
