package com.Consultory.app.controller;

import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.service.ConsultRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ConsultRoom")
@RequiredArgsConstructor
public class ConsultRoomController {
    private final ConsultRoomService consultRoomService;

    @PostMapping
    public ResponseEntity<ConsultRoomDTO> createConsultRoom(@Valid @RequestBody ConsultRoomDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultRoomService.createConsultRoom(dto));
    }
    @GetMapping
    public ResponseEntity<List<ConsultRoomDTO>> getAllConsultRooms() {
        return ResponseEntity.status(HttpStatus.OK).body(consultRoomService.getAllConsultRooms());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ConsultRoomDTO> getConsultRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(consultRoomService.getConsultRoomById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ConsultRoomDTO> updateConsultRoom(@PathVariable Long id, @Valid @RequestBody ConsultRoomDTO dto) {
        return ResponseEntity.ok(consultRoomService.updateConsultRoom(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultRoom(@PathVariable Long id) {
        consultRoomService.deleteConsultRoom(id);
        return ResponseEntity.noContent().build();
    }
}
