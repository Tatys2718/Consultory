package com.Consultory.app.mapper;

import com.Consultory.app.dto.MedicalRecordDTO;
import com.Consultory.app.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "patient.id", target = "patientId")
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);
    @Mapping(source = "created", target = "created")
    @Mapping (target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    MedicalRecord toEntity(MedicalRecordDTO dto);

}
