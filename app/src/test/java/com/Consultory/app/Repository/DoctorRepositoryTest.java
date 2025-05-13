package com.Consultory.app.Repository;

import com.Consultory.app.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Testcontainers
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;
    @Test
    void shouldFindBySpecialtyIgnoreCase() {
        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Camila")
                .speciality("Cardiology")
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Andrés")
                .speciality("cardiology")
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Dr. Esteban")
                .speciality("Neurology")
                .build());

        List<Doctor> found = doctorRepository.findBySpecialityIgnoreCase("CARDIOLOGY");

        assertEquals(2, found.size());
        assertTrue(found.stream().allMatch(doc -> doc.getSpeciality().equalsIgnoreCase("cardiology")));
    }
}