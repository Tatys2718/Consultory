package com.Consultory.app.service;

import com.Consultory.app.dto.AppointmentDTO;

import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO dto);
    List<AppointmentDTO> getAllAppointments();
    AppointmentDTO getAppointmentById(Long id);
    AppointmentDTO updateAppointment(Long id, AppointmentDTO dto);
    AppointmentDTO cancelAppointment(Long id);
    void deleteAppointment(Long id);
}
