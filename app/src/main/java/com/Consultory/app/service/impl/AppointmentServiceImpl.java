package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.AppointmentRepository;
import com.Consultory.app.Repository.ConsultRoomRepository;
import com.Consultory.app.Repository.DoctorRepository;
import com.Consultory.app.Repository.PatientRepository;
import com.Consultory.app.dto.AppointmentDTO;
import com.Consultory.app.exception.*;
import com.Consultory.app.mapper.AppointmentMapper;
import com.Consultory.app.model.*;
import com.Consultory.app.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultRoomRepository consultRoomRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + dto.getDoctorId()));
        ConsultRoom consultRoom = consultRoomRepository.findById(dto.getConsultRoomId()).
                orElseThrow(()-> new RuntimeException("ConsultRoom not found with ID: " + dto.getConsultRoomId()));
        List<Appointment> conflicts = appointmentRepository.findConflicts(consultRoom.getId(), doctor.getId(), dto.getStartTime(), dto.getEndTime());

        if(!conflicts.isEmpty()){
            throw new ConsultRoomAlreadyAppointmentException("The Consult room is already appointed for the selected time slot.");
        }
        long duration = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();

        if(duration < 20 || duration > 180){
            throw new InvalidAppointmentException("The duration of the appointment should between 20 and 180 minutes");
        }

        if (dto.getStartTime().toLocalTime().isBefore(doctor.getAvailableFrom()) ||
                dto.getEndTime().toLocalTime().isAfter(doctor.getAvailableTo())) {
            throw new InvalidAppointmentDoctor("The appointment is out of range of the doctor");
        }

        if (dto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentTime("The appointment should be in the future.");
        }

        Appointment appointment = appointmentMapper.toEntity(dto);
        appointment.setPatient(patient);
        appointment.setConsultRoom(consultRoom);
        appointment.setDoctor(doctor);

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }
    @Override
    public List<AppointmentDTO> getAllAppointments(){
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }
    @Override
    public AppointmentDTO getAppointmentById(Long id){
        return appointmentRepository.findById(id)
                .map(appointmentMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }
    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO dto){
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));

        if(existing.getStatus() == AppointmentStatus.COMPLETED){
            throw new StatusAppointmentCompletedException("The appointment existing is completed");
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + dto.getPatientId()));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + dto.getDoctorId()));
        ConsultRoom consultRoom = consultRoomRepository.findById(dto.getConsultRoomId()).
                orElseThrow(()-> new RuntimeException("ConsultRoom not found with ID: " + dto.getConsultRoomId()));

        List<Appointment> conflicts = appointmentRepository.findConflicts(consultRoom.getId(), doctor.getId(), dto.getStartTime(), dto.getEndTime())
                .stream().filter(b -> !b.getId().equals(id)).toList();

        if (!conflicts.isEmpty()) {
            throw new ConsultRoomAlreadyAppointmentException("The Consult room is already appointed for the selected time slot.");
        }
        long duration = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();

        if(duration < 20 || duration > 180){
            throw new InvalidAppointmentException("The duration of the appointment it should between 20 and 180 minutes");
        }

        if (dto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentTime("The appointment should be in the future.");
        }
        existing.setPatient(patient);
        existing.setConsultRoom(consultRoom);
        existing.setDoctor(doctor);
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());

        return appointmentMapper.toDTO(appointmentRepository.save(existing));
    }
    @Override
    public AppointmentDTO cancelAppointment(Long id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("You can´t cancel an appointment completed");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }
    @Override
    public List<AppointmentDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository
                .findByDoctorIdAndStartTimeBetween(doctorId, startOfDay, endOfDay)
                .stream()
                .map(appointmentMapper::toDTO)
                .toList();
    }
    @Override
    public void deleteAppointment(Long id){
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}
