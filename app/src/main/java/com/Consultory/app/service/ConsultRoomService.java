package com.Consultory.app.service;


import com.Consultory.app.dto.ConsultRoomDTO;

import java.util.List;

public interface ConsultRoomService {
    ConsultRoomDTO createConsultRoom(ConsultRoomDTO dto);
    List<ConsultRoomDTO> getAllConsultRooms();
    ConsultRoomDTO getConsultRoomById(Long id);
    ConsultRoomDTO updateConsultRoom(Long id, ConsultRoomDTO dto);
    void deleteConsultRoom(Long id);
}
