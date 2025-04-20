package com.Consultory.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "MedicalRecords")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String diagnosis;

    @NotBlank
    @Column(nullable = false)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime created;

    @OneToOne
    @JoinColumn(name = "IdAppointment")
    private Appointment appointment;
    @ManyToOne
    @JoinColumn(name = "IdPatient")
    private Patient patient;
}
