package com.Consultory.app.Repository;

import com.Consultory.app.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Testcontainers
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void shouldDetectConflictingAppointment() {
        LocalTime StartHourDoctor = LocalTime.of(9, 0);
        LocalTime EndHourDoctor = LocalTime.of(17, 0);
        Patient patient = patientRepository.save(Patient.builder().fullName("Karen").email("ktmora@test.com").build());
        ConsultRoom consultRoom = consultRoomRepository.save(ConsultRoom.builder().name("RoomA").floor(2).description("TestDescription").build());
        Doctor doctor = doctorRepository.save(Doctor.builder().fullName("Joseph").email("joseph@test.com").speciality("Psychiatry").availableFrom(StartHourDoctor).availableTo(EndHourDoctor).build());

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        LocalDateTime end = start.plusHours(1);

        appointmentRepository.save(Appointment.builder()
                .doctor(doctor)
                .consultRoom(consultRoom)
                .patient(patient)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
                .build());
        List<Appointment> conflicts = appointmentRepository.findConflicts(
                consultRoom.getId(),
                doctor.getId(),
                start.plusMinutes(30),
                end.plusHours(1)
        );
        assertFalse(conflicts.isEmpty());
    }

    @Test
    void shouldFindByDoctorIdAndStartTimeBetween() {
        
    }
}