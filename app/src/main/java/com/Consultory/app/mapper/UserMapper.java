package com.Consultory.app.mapper;

import com.Consultory.app.dto.PatientDTO;
import com.Consultory.app.dto.UserDTO;
import com.Consultory.app.model.Patient;
import com.Consultory.app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO (User patient);
    User toEntity (UserDTO dto);
}
