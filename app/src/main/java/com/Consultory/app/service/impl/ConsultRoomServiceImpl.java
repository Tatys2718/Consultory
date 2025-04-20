package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.ConsultRoomRepository;
import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.exception.ResourceNotFoundException;
import com.Consultory.app.mapper.ConsultRoomMapper;
import com.Consultory.app.model.ConsultRoom;
import com.Consultory.app.service.ConsultRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultRoomServiceImpl implements ConsultRoomService {
    private final ConsultRoomRepository consultRoomRepository;
    private final ConsultRoomMapper consultRoomMapper;

    @Override
    public ConsultRoomDTO createConsultRoom(ConsultRoomDTO dto) {
        ConsultRoom room = consultRoomMapper.toEntity(dto);
        return consultRoomMapper.toDTO(consultRoomRepository.save(room));
    }

    @Override
    public List<ConsultRoomDTO> getAllConsultRooms() {
        return consultRoomRepository.findAll().stream().map(consultRoomMapper::toDTO).toList();
    }

    @Override
    public ConsultRoomDTO getConsultRoomById(Long id) {
        return consultRoomRepository.findById(id)
                .map(consultRoomMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Consult Room not found with ID: " + id));
    }

    @Override
    public ConsultRoomDTO updateConsultRoom(Long id, ConsultRoomDTO dto) {
        ConsultRoom existing = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consult Room not found with ID: " + id));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setFloor(dto.getFloor());
        return consultRoomMapper.toDTO(consultRoomRepository.save(existing));
    }

    @Override
    public void deleteConsultRoom(Long id) {
        if (!consultRoomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consult Room not found with ID: " + id);
        }
        consultRoomRepository.deleteById(id);
    }

}
