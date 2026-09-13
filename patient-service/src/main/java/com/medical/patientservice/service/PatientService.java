package com.medical.patientservice.service;

import com.medical.patientservice.dto.PatientRequest;
import com.medical.patientservice.entity.Patient;
import java.util.List;

public interface PatientService {
    Patient savePatient(PatientRequest request);
    List<Patient> getAllPatients();
    Patient getPatientById(Long id);
}
