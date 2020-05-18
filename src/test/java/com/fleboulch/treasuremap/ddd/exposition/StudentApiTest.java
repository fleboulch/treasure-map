package com.fleboulch.treasuremap.ddd.exposition;

import com.fleboulch.treasuremap.ddd.app.StudentCRUD;
import com.fleboulch.treasuremap.ddd.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = StudentApi.class)
class StudentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentCRUD service;

    @Test
    void it_should_fetch_student_by_id() throws Exception {

        UUID id = UUID.randomUUID();
        when(service.findById(any())).thenReturn(buildStudent(id));

        String url = "/api/students";

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Student buildStudent(UUID id) {
        return new Student(id, "John", "Doe", 20);
    }
}
