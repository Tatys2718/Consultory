package com.Consultory.app.service;

import com.Consultory.app.dto.AppointmentDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO dto);
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    AppointmentDTO updateAppointment(Long id, AppointmentDTO dto);
    AppointmentDTO cancelAppointment(Long id);
    List<AppointmentDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date);
    void deleteAppointment(Long id);
}
