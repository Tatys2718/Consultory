package com.Consultory.app.mapper;

import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.model.Doctor;
import com.Consultory.app.model.ERol;
import com.Consultory.app.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "user.id", target = "userId")
    DoctorDTO toDto(Doctor doctor);
    @Mapping (target = "user", ignore = true)
    Doctor toEntity(DoctorDTO dto);
}
