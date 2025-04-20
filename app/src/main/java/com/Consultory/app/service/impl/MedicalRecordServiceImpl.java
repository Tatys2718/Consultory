package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.AppointmentRepository;
import com.Consultory.app.Repository.MedicalRecordRepository;
import com.Consultory.app.Repository.PatientRepository;
import com.Consultory.app.dto.MedicalRecordDTO;
import com.Consultory.app.mapper.MedicalRecordMapper;
import com.Consultory.app.model.Appointment;
import com.Consultory.app.model.AppointmentStatus;
import com.Consultory.app.model.MedicalRecord;
import com.Consultory.app.model.Patient;
import com.Consultory.app.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl extends MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto){
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + dto.getPatientId()));
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + dto.getPatientId()));

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Medical records can only be created for completed appointments.");
        }
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(dto);
        medicalRecord.setPatient(patient);
        medicalRecord.setAppointment(appointment);

        return medicalRecordMapper.toDTO(medicalRecordRepository.save(medicalRecord));
    }
    @Override
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(medicalRecordMapper::toDTO)
                .toList();
    }
    @Override
    public MedicalRecordDTO getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id).map(medicalRecordMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO dto){
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Record not found with ID: " + id));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + dto.getPatientId()));
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + dto.getPatientId()));
        if(appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Medical records can only be updated for completed appointments.");
        }

        existing.setPatient(patient);
        existing.setAppointment(appointment);
        existing.setDiagnosis(dto.getDiagnosis());
        existing.setNotes(dto.getNotes());
        existing.setCreated(dto.getCreatedAt());

        return medicalRecordMapper.toDTO(medicalRecordRepository.save(existing));
    }
    @Override
    public void deleteMedicalRecord(Long id) {
        if(!medicalRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medical Record not found with ID: " + id);
        }

        medicalRecordRepository.deleteById(id);
    }
}
