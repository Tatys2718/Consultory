package com.Consultory.app.service;

import com.Consultory.app.Repository.AppointmentRepository;
import com.Consultory.app.Repository.MedicalRecordRepository;
import com.Consultory.app.Repository.PatientRepository;
import com.Consultory.app.dto.MedicalRecordDTO;
import com.Consultory.app.mapper.MedicalRecordMapper;
import com.Consultory.app.model.Appointment;
import com.Consultory.app.model.AppointmentStatus;
import com.Consultory.app.model.MedicalRecord;
import com.Consultory.app.model.Patient;
import com.Consultory.app.service.impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @Test
    void shouldCreateMedicalRecord() {
        Long patientId = 1L;
        Long appointmentId = 10L;

        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .id(100L)
                .patientId(patientId)
                .appointmentId(appointmentId)
                .diagnosis("Diagnosis test")
                .notes("Rest and hydration")
                .createdAt(LocalDateTime.now())
                .build();

        Patient patient = Patient.builder().id(patientId).fullName("John Doe").build();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.COMPLETED)
                .build();


        MedicalRecord medicalRecord = MedicalRecord.builder()
                .diagnosis(dto.getDiagnosis())
                .notes(dto.getNotes())
                .created(dto.getCreatedAt())
                .patient(patient)
                .appointment(appointment)
                .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(medicalRecordMapper.toEntity(dto)).thenReturn(medicalRecord);
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(medicalRecord);
        when(medicalRecordMapper.toDTO(medicalRecord)).thenReturn(dto);

        MedicalRecordDTO result = medicalRecordService.createMedicalRecord(dto);

        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDiagnosis(), result.getDiagnosis());
    }

    @Test
    void shouldGetAllMedicalRecords() {
        Patient patient = Patient.builder()
                .id(1L)
                .fullName("Jane Doe")
                .build();

        Appointment appointment = Appointment.builder()
                .id(100L)
                .status(AppointmentStatus.COMPLETED)
                .build();

        MedicalRecord medicalRecord1 = MedicalRecord.builder()
                .id(1L)
                .diagnosis("Diagnosis 1")
                .notes("Notes 1")
                .created(LocalDateTime.now())
                .patient(patient)
                .appointment(appointment)
                .build();

        MedicalRecord medicalRecord2 = MedicalRecord.builder()
                .id(2L)
                .diagnosis("Diagnosis 2")
                .notes("Notes 2")
                .created(LocalDateTime.now())
                .patient(patient)
                .appointment(appointment)
                .build();

        MedicalRecordDTO dto1 = MedicalRecordDTO.builder()
                .id(1L)
                .diagnosis("Diagnosis 1")
                .notes("Notes 1")
                .createdAt(medicalRecord1.getCreated())
                .patientId(patient.getId())
                .appointmentId(appointment.getId())
                .build();

        MedicalRecordDTO dto2 = MedicalRecordDTO.builder()
                .id(2L)
                .diagnosis("Diagnosis 2")
                .notes("Notes 2")
                .createdAt(medicalRecord2.getCreated())
                .patientId(patient.getId())
                .appointmentId(appointment.getId())
                .build();

        // Mocks
        when(medicalRecordRepository.findAll()).thenReturn(List.of(medicalRecord1, medicalRecord2));
        when(medicalRecordMapper.toDTO(medicalRecord1)).thenReturn(dto1);
        when(medicalRecordMapper.toDTO(medicalRecord2)).thenReturn(dto2);

        // Ejecución
        List<MedicalRecordDTO> result = medicalRecordService.getAllMedicalRecords();

        // Verificación
        assertEquals(2, result.size());
        assertEquals("Diagnosis 1", result.get(0).getDiagnosis());
        assertEquals("Diagnosis 2", result.get(1).getDiagnosis());

        verify(medicalRecordRepository).findAll();
        verify(medicalRecordMapper).toDTO(medicalRecord1);
        verify(medicalRecordMapper).toDTO(medicalRecord2);
    }

    @Test
    void getMedicalRecordById() {
        Long id = 1L;

        Patient patient = Patient.builder()
                .id(100L)
                .fullName("Jane Doe")
                .build();

        Appointment appointment = Appointment.builder()
                .id(200L)
                .status(AppointmentStatus.COMPLETED)
                .build();

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .id(id)
                .diagnosis("Diagnosis Test")
                .notes("Prescribe rest")
                .created(LocalDateTime.now())
                .patient(patient)
                .appointment(appointment)
                .build();

        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .id(id)
                .diagnosis("Diagnosis Test")
                .notes("Prescribe rest")
                .createdAt(medicalRecord.getCreated())
                .patientId(patient.getId())
                .appointmentId(appointment.getId())
                .build();

        when(medicalRecordRepository.findById(id)).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordMapper.toDTO(medicalRecord)).thenReturn(dto);

        MedicalRecordDTO result = medicalRecordService.getMedicalRecordById(id);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDiagnosis(), result.getDiagnosis());
        assertEquals(dto.getPatientId(), result.getPatientId());

        verify(medicalRecordRepository).findById(id);
        verify(medicalRecordMapper).toDTO(medicalRecord);
    }

    @Test
    void shouldGetMedicalRecordsByPatientId() {
        Long patientId = 1L;

        MedicalRecord record1 = MedicalRecord.builder()
                .id(101L)
                .diagnosis("Diagnosis A")
                .notes("Notes A")
                .created(LocalDateTime.now())
                .patient(Patient.builder().id(patientId).build())
                .appointment(Appointment.builder().id(201L).status(AppointmentStatus.COMPLETED).build())
                .build();

        MedicalRecord record2 = MedicalRecord.builder()
                .id(102L)
                .diagnosis("Diagnosis B")
                .notes("Notes B")
                .created(LocalDateTime.now())
                .patient(Patient.builder().id(patientId).build())
                .appointment(Appointment.builder().id(202L).status(AppointmentStatus.COMPLETED).build())
                .build();

        MedicalRecordDTO dto1 = MedicalRecordDTO.builder()
                .id(101L)
                .diagnosis("Diagnosis A")
                .notes("Notes A")
                .createdAt(record1.getCreated())
                .patientId(patientId)
                .appointmentId(201L)
                .build();

        MedicalRecordDTO dto2 = MedicalRecordDTO.builder()
                .id(102L)
                .diagnosis("Diagnosis B")
                .notes("Notes B")
                .createdAt(record2.getCreated())
                .patientId(patientId)
                .appointmentId(202L)
                .build();

        when(medicalRecordRepository.findByPatientId(patientId)).thenReturn(List.of(record1, record2));
        when(medicalRecordMapper.toDTO(record1)).thenReturn(dto1);
        when(medicalRecordMapper.toDTO(record2)).thenReturn(dto2);

        List<MedicalRecordDTO> results = medicalRecordService.getMedicalRecordsByPatientId(patientId);

        assertEquals(2, results.size());
        assertEquals(dto1.getId(), results.get(0).getId());
        assertEquals(dto2.getId(), results.get(1).getId());
    }

    @Test
    void updateMedicalRecord() {
        Long id = 1L;
        Long patientId = 10L;
        Long appointmentId = 20L;

        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .patientId(patientId)
                .appointmentId(appointmentId)
                .diagnosis("Updated Diagnosis")
                .notes("Updated Notes")
                .createdAt(LocalDateTime.now())
                .build();

        Patient patient = Patient.builder().id(patientId).fullName("Updated Patient").build();

        Appointment appointment = Appointment.builder()
                .id(appointmentId)
                .status(AppointmentStatus.COMPLETED)
                .build();

        MedicalRecord existing = MedicalRecord.builder()
                .id(id)
                .diagnosis("Old Diagnosis")
                .notes("Old Notes")
                .created(LocalDateTime.now())
                .build();

        MedicalRecord updated = MedicalRecord.builder()
                .id(id)
                .diagnosis(dto.getDiagnosis())
                .notes(dto.getNotes())
                .created(dto.getCreatedAt())
                .patient(patient)
                .appointment(appointment)
                .build();

        MedicalRecordDTO resultDTO = MedicalRecordDTO.builder()
                .id(id)
                .patientId(dto.getPatientId())
                .appointmentId(dto.getAppointmentId())
                .diagnosis(dto.getDiagnosis())
                .notes(dto.getNotes())
                .createdAt(dto.getCreatedAt())
                .build();

        when(medicalRecordRepository.findById(id)).thenReturn(Optional.of(existing));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(medicalRecordRepository.save(existing)).thenReturn(updated);
        when(medicalRecordMapper.toDTO(updated)).thenReturn(resultDTO);

        MedicalRecordDTO result = medicalRecordService.updateMedicalRecord(id, dto);

        assertEquals(resultDTO.getId(), result.getId());
        assertEquals(resultDTO.getDiagnosis(), result.getDiagnosis());
        assertEquals(resultDTO.getNotes(), result.getNotes());
    }

    @Test
    void shouldDeleteMedicalRecord() {
        Long id = 1L;

        when(medicalRecordRepository.existsById(id)).thenReturn(true);

        medicalRecordService.deleteMedicalRecord(id);

        verify(medicalRecordRepository).existsById(id);
        verify(medicalRecordRepository).deleteById(id);
    }
}