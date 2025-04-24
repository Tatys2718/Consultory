package com.Consultory.app.mapper;

import com.Consultory.app.dto.PatientDTO;
import com.Consultory.app.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toDTO (Patient patient);
    Patient toEntity (PatientDTO dto);
}
