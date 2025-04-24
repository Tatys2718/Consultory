package com.Consultory.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalRecordDTO {
    private Long id;

    @NotBlank
    private String diagnosis;
    @NotBlank
    private String notes;
    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long patientId;
}
