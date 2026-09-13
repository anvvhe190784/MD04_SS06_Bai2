package com.medical.patientservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.patientservice.controller.PatientController;
import com.medical.patientservice.dto.PatientRequest;
import com.medical.patientservice.entity.Patient;
import com.medical.patientservice.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreatePatient() throws Exception {
        PatientRequest request = PatientRequest.builder()
                .fullName("Nguyen Van A")
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .gender("Nam")
                .phoneNumber("0912345678")
                .address("Ha Noi")
                .medicalHistory("Khong co tien su benh ly")
                .build();

        Patient saved = Patient.builder()
                .id(1L)
                .fullName("Nguyen Van A")
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .gender("Nam")
                .phoneNumber("0912345678")
                .address("Ha Noi")
                .medicalHistory("Khong co tien su benh ly")
                .build();

        Mockito.when(patientService.savePatient(Mockito.any(PatientRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Nguyen Van A"))
                .andExpect(jsonPath("$.address").value("Ha Noi"));
    }
}
