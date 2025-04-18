package com.Consultory.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "Doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    private String speciality;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime availableFrom;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime availableTo;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;
}
