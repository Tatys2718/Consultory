package com.Consultory.app.controller;

import com.Consultory.app.dto.MedicalRecordDTO;
import com.Consultory.app.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/MedicalRecord")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@Valid @RequestBody MedicalRecordDTO medicalRecordDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.createMedicalRecord(medicalRecordDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id){
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(id));
    }

    @GetMapping("patient/{patientId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByPatientId(@PathVariable Long patientId){
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecordsByPatientId(patientId);
        return ResponseEntity.ok(records);
    }
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecordDTO medicalRecordDTO){
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, medicalRecordDTO));
    }
    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id){
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
