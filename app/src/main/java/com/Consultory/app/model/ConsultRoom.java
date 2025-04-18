package com.Consultory.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "ConsultRooms")
public class ConsultRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private int floor;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @OneToMany (mappedBy = "consultRoom")
    private Set<Appointment> appointments;

}
