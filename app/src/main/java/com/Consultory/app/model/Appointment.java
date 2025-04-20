package com.Consultory.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Future
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn (name = "IdPatient")
    private Patient patient;
    @ManyToOne
    @JoinColumn (name = "IdConsultRoom")
    private ConsultRoom consultRoom;
    @ManyToOne
    @JoinColumn (name = "IdDoctor")
    private Doctor doctor;
    @OneToOne(mappedBy = "appointment")
    private MedicalRecord medicalRecord;
}
