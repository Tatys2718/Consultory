package com.Consultory.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    private String email;

    private String speciality;

    @NotNull
    private LocalTime availableFrom;
    @NotNull
    private LocalTime availableTo;

}
