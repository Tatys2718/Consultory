package com.Consultory.app.Repository;

import com.Consultory.app.model.Doctor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DoctorRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");
    
    @Autowired
    private DoctorRepository doctorRepository;
    @Test
    void shouldFindBySpecialtyIgnoreCase() {
        LocalTime availableFrom = LocalTime.of(9, 0);
        LocalTime availableTo = LocalTime.of(17, 0);
        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Camila")
                .email("camila@test.com")
                .speciality("Cardiology")
                .availableFrom(availableFrom)
                .availableTo(availableTo)
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Andrés")
                .email("andres@test.com")
                .speciality("cardiology")
                .availableFrom(availableFrom)
                .availableTo(availableTo)
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Esteban")
                .email("esteban@test.com")
                .speciality("Neurology")
                .availableFrom(availableFrom)
                .availableTo(availableTo)
                .build());

        List<Doctor> found = doctorRepository.findBySpecialityIgnoreCase("CARDIOLOGY");

        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(doc -> doc.getSpeciality().equalsIgnoreCase("cardiology")));
    }
}