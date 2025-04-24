package com.Consultory.app.service;

import com.Consultory.app.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    PatientDTO createPatient(PatientDTO dto);
    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(Long id);
    PatientDTO updatePatient(Long id, PatientDTO dto);
    void deletePatient(Long id);
}
