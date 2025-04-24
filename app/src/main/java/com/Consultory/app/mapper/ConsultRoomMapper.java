package com.Consultory.app.mapper;

import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.model.ConsultRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultRoomMapper {
    ConsultRoomDTO toDTO(ConsultRoom consultRoom);
    ConsultRoom toEntity(ConsultRoomDTO dto);
}
