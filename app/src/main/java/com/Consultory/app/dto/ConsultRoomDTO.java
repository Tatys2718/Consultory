package com.Consultory.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultRoomDTO {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private int floor;

    @NotBlank
    private String description;

}
