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
        Doctor doctor = doctorRepository.save(Doctor.builder().fullName("Dr. Smith").build());
        Patient patient = patientRepository.save(Patient.builder().fullName("John Doe").build());
        ConsultRoom room = consultRoomRepository.save(ConsultRoom.builder().name("RoomB").build());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(2);

        Appointment appointment1 = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .consultRoom(room)
                .startTime(start.plusHours(1))
                .endTime(start.plusHours(2))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment appointment2 = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .consultRoom(room)
                .startTime(end.plusDays(1))
                .endTime(end.plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        appointmentRepository.saveAll(List.of(appointment1, appointment2));

        List<Appointment> found = appointmentRepository.findByDoctorIdAndStartTimeBetween(
                doctor.getId(), start, end
        );

        assertEquals(1, found.size());
        assertEquals(appointment1.getStartTime(), found.get(0).getStartTime());
    }
}