package com.Consultory.app.mapper;

import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDto(Doctor doctor);
    Doctor toEntity(DoctorDTO dto);
}
