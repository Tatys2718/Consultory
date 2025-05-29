package com.Consultory.app.dto;

import com.Consultory.app.model.ERol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

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
    @NotNull
    private Long userId;
}
