package com.Consultory.app.service;

import com.Consultory.app.Repository.ConsultRoomRepository;
import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.mapper.ConsultRoomMapper;
import com.Consultory.app.model.ConsultRoom;
import com.Consultory.app.service.impl.ConsultRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultRoomServiceTest {
    @Mock
    private ConsultRoomRepository consultRoomRepository;
    @Mock
    private ConsultRoomMapper consultRoomMapper;
    @InjectMocks
    private ConsultRoomServiceImpl consultRoomService;

    @Test
    void shouldCreateConsultRoom() {
        ConsultRoomDTO dto = ConsultRoomDTO.builder().name("Room A").floor(2).description("Description A").build();
        ConsultRoom consultRoom = ConsultRoom.builder().name("Room A").floor(2).description("Description A").build();

        when(consultRoomMapper.toEntity(dto)).thenReturn(consultRoom);
        when(consultRoomRepository.save(consultRoom)).thenReturn(consultRoom);
        when(consultRoomMapper.toDTO(consultRoom)).thenReturn(dto);

        ConsultRoomDTO result = consultRoomService.createConsultRoom(dto);
        verify(consultRoomRepository).save(consultRoom);
    }

    @Test
    void shouldGetAllConsultRooms() {
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).name("Room B").floor(2).description("Description B").build();
        ConsultRoomDTO dto = ConsultRoomDTO.builder().id(1L).name("Room B").floor(2).description("Description B").build();

        when(consultRoomRepository.findAll()).thenReturn(List.of(consultRoom));
        when(consultRoomMapper.toDTO(consultRoom)).thenReturn(dto);

        List<ConsultRoomDTO> consultRooms = consultRoomService.getAllConsultRooms();

        assertEquals(1, consultRooms.size());
        assertEquals("Room B", consultRooms.get(0).getName());
    }

    @Test
    void shouldGetConsultRoomById() {
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).name("Room A").build();
        ConsultRoomDTO dto = ConsultRoomDTO.builder().id(1L).name("Room A").build();

        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(consultRoomMapper.toDTO(consultRoom)).thenReturn(dto);

        ConsultRoomDTO result = consultRoomService.getConsultRoomById(1L);

        assertEquals("Room A", result.getName());
    }

    @Test
    void shouldUpdateConsultRoom() {
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).name("Room A").floor(3).description("Description test").build();
        ConsultRoomDTO dto = ConsultRoomDTO.builder().name("Room A Updated").floor(4).description("Description test updated").build();

        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(consultRoomRepository.save(any())).thenReturn(consultRoom);
        when(consultRoomMapper.toDTO(any())).thenReturn(dto);

        ConsultRoomDTO result = consultRoomService.updateConsultRoom(1L, dto);

        assertEquals("Room A Updated", result.getName());
    }

    @Test
    void shouldDeleteConsultRoom() {
        when(consultRoomRepository.existsById(1L)).thenReturn(true);

        consultRoomService.deleteConsultRoom(1L);

        verify(consultRoomRepository).deleteById(1L);
    }
}