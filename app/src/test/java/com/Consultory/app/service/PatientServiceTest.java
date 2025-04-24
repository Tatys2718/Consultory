package com.Consultory.app.service;

import com.Consultory.app.Repository.PatientRepository;
import com.Consultory.app.dto.PatientDTO;
import com.Consultory.app.mapper.PatientMapper;
import com.Consultory.app.model.Patient;
import com.Consultory.app.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void shouldGetPatientById() {
        Patient patient = Patient.builder().id(1L).fullName("Camila").email("camila@test.com").build();
        PatientDTO dto = PatientDTO.builder().id(1L).fullName("Camila").email("camila@test.com").build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDTO(patient)).thenReturn(dto);

        PatientDTO result = patientService.getPatientById(1L);

        assertEquals("Camila", result.getFullName());
    }

    @Test
    void shouldGetAllPatients() {
        Patient patient = Patient.builder().fullName("Camila").email("camila@test.com").build();
        PatientDTO dto = PatientDTO.builder().fullName("Camila").email("camila@test.com").build();
        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientMapper.toDTO(patient)).thenReturn(dto);

        List<PatientDTO> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        assertEquals("Camila", result.get(0).getFullName());

    }

    @Test
    void shouldCreatePatient() {
        PatientDTO patientDTO = PatientDTO.builder().fullName("Camila").email("camila@test.com").phone("3147586421").build();
        Patient patient = Patient.builder().fullName("Camila").email("camila@test.com").phone("3147586421").build();
        when(patientMapper.toEntity(patientDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDTO(patient)).thenReturn(patientDTO);
        PatientDTO result = patientService.createPatient(patientDTO);
        assertEquals("Camila", result.getFullName());
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldUpdatePatient() {
        PatientDTO input = PatientDTO.builder().fullName("Karen Updated").email("karen@update.com").build();
        Patient patient = Patient.builder().id(1L).fullName("Karen").email("karen@test.com").build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any())).thenReturn(patient);
        when(patientMapper.toDTO(any())).thenReturn(input);

        PatientDTO result = patientService.updatePatient(1L, input);

        assertEquals("Karen Updated", result.getFullName());
    }

    @Test
    void shouldDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.deletePatient(1L);

        verify(patientRepository).deleteById(1L);
    }
}