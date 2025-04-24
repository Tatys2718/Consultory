package com.Consultory.app.service;

import com.Consultory.app.Repository.DoctorRepository;
import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.exception.ResourceNotFoundException;
import com.Consultory.app.mapper.DoctorMapper;
import com.Consultory.app.model.Doctor;
import com.Consultory.app.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void shouldCreateDoctor() {
        DoctorDTO dto = DoctorDTO.builder()
                .fullName("Dr. House")
                .email("house@example.com")
                .speciality("Diagnostics")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        Doctor doctor = Doctor.builder()
                .fullName("Dr. House")
                .email("house@example.com")
                .speciality("Diagnostics")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        when(doctorMapper.toEntity(dto)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        DoctorDTO result = doctorService.createDoctor(dto);

        assertEquals("Dr. House", result.getFullName());
        verify(doctorRepository).save(doctor);
    }

    @Test
    void shouldGetAllDoctors() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .fullName("Dr. Grey")
                .email("grey@example.com")
                .speciality("Surgery")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .build();

        DoctorDTO dto = DoctorDTO.builder()
                .id(1L)
                .fullName("Dr. Grey")
                .email("grey@example.com")
                .speciality("Surgery")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .build();

        when(doctorRepository.findAll()).thenReturn(List.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        List<DoctorDTO> result = doctorService.getAllDoctors();

        assertEquals(1, result.size());
        assertEquals("Dr. Grey", result.get(0).getFullName());
    }

    @Test
    void shouldGetDoctorById() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .fullName("Dr. Wilson")
                .build();

        DoctorDTO dto = DoctorDTO.builder()
                .id(1L)
                .fullName("Dr. Wilson")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        DoctorDTO result = doctorService.getDoctorById(1L);

        assertEquals("Dr. Wilson", result.getFullName());
    }

    @Test
    void shouldGetDoctorsBySpecialty() {
        String specialty = "Cardiology";

        Doctor doctor = Doctor.builder()
                .id(1L)
                .speciality("Cardiology")
                .fullName("Dr. Carter")
                .build();

        DoctorDTO dto = DoctorDTO.builder()
                .id(1L)
                .speciality("Cardiology")
                .fullName("Dr. Carter")
                .build();

        when(doctorRepository.findBySpecialtyIgnoreCase(specialty)).thenReturn(List.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        List<DoctorDTO> result = doctorService.getDoctorsBySpecialty(specialty);

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpeciality());
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor existing = Doctor.builder()
                .id(1L)
                .fullName("Dr. Old")
                .email("old@example.com")
                .speciality("Old Specialty")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(12, 0))
                .build();

        DoctorDTO updateDTO = DoctorDTO.builder()
                .fullName("Dr. Updated")
                .email("updated@example.com")
                .speciality("Updated Specialty")
                .availableFrom(LocalTime.of(10, 0))
                .availableTo(LocalTime.of(18, 0))
                .build();

        Doctor updated = Doctor.builder()
                .id(1L)
                .fullName("Dr. Updated")
                .email("updated@example.com")
                .speciality("Updated Specialty")
                .availableFrom(LocalTime.of(10, 0))
                .availableTo(LocalTime.of(18, 0))
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(doctorRepository.save(existing)).thenReturn(updated);
        when(doctorMapper.toDto(updated)).thenReturn(updateDTO);

        DoctorDTO result = doctorService.updateDoctor(1L, updateDTO);

        assertEquals("Dr. Updated", result.getFullName());
        assertEquals("Updated Specialty", result.getSpeciality());
    }

    @Test
    void shouldDeleteDoctor() {
        when(doctorRepository.existsById(1L)).thenReturn(true);

        doctorService.deleteDoctor(1L);

        verify(doctorRepository).deleteById(1L);
    }
}