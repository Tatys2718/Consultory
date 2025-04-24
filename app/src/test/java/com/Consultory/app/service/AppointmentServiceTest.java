package com.Consultory.app.service;

import com.Consultory.app.Repository.AppointmentRepository;
import com.Consultory.app.Repository.ConsultRoomRepository;
import com.Consultory.app.Repository.DoctorRepository;
import com.Consultory.app.Repository.PatientRepository;
import com.Consultory.app.dto.AppointmentDTO;
import com.Consultory.app.exception.ConsultRoomAlreadyAppointmentException;
import com.Consultory.app.mapper.AppointmentMapper;
import com.Consultory.app.model.*;
import com.Consultory.app.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private ConsultRoomRepository consultRoomRepository;
    @Mock private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void shouldCreateAppointment() {
        Long patientId = 1L;
        Long doctorId = 2L;
        Long consultRoomId = 3L;

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        LocalDateTime end = start.plusMinutes(30);

        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .consultRoomId(consultRoomId)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Patient patient = Patient.builder().id(patientId).build();
        Doctor doctor = Doctor.builder().id(doctorId)
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(18, 0))
                .build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(consultRoomId).build();

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(consultRoomRepository.findById(consultRoomId)).thenReturn(Optional.of(consultRoom));
        when(appointmentRepository.findConflicts(consultRoomId, doctorId, start, end)).thenReturn(List.of());
        when(appointmentMapper.toEntity(dto)).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.toDTO(appointment)).thenReturn(dto);

        AppointmentDTO result = appointmentService.createAppointment(dto);

        assertEquals(patientId, result.getPatientId());
        assertEquals(doctorId, result.getDoctorId());
        assertEquals(consultRoomId, result.getConsultRoomId());
    }

    @Test
    void shouldThrowIfConsultRoomIsBooked() {
        Long consultRoomId = 3L;
        Long doctorId = 2L;
        Long patientId = 1L;

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        LocalDateTime end = start.plusMinutes(30);

        AppointmentDTO dto = AppointmentDTO.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .consultRoomId(consultRoomId)
                .startTime(start)
                .endTime(end)
                .build();

        Patient patient = Patient.builder().id(patientId).build();
        Doctor doctor = Doctor.builder().id(doctorId)
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(18, 0))
                .build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(consultRoomId).build();

        Appointment conflicting = Appointment.builder().id(999L).build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(consultRoomRepository.findById(consultRoomId)).thenReturn(Optional.of(consultRoom));
        when(appointmentRepository.findConflicts(consultRoomId, doctorId, start, end))
                .thenReturn(List.of(conflicting));

        assertThrows(ConsultRoomAlreadyAppointmentException.class, () ->
                appointmentService.createAppointment(dto));
    }
}