package com.Consultory.app.service;

import com.Consultory.app.dto.MedicalRecordDTO;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto);
    List<MedicalRecordDTO> getAllMedicalRecords();
    MedicalRecordDTO getMedicalRecordById(Long id);
    MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO dto);
    void deleteMedicalRecord(Long id);
}
