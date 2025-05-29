package com.Consultory.app.controller;

import com.Consultory.app.dto.AppointmentDTO;
import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.model.AppointmentStatus;
import com.Consultory.app.security.jwt.JwtFilter;
import com.Consultory.app.security.jwt.JwtUtil;
import com.Consultory.app.security.services.UserInfoService;
import com.Consultory.app.service.AppointmentService;
import com.Consultory.app.service.ConsultRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(ConsultRoomController.class)
@Import({ConsultRoomControllerTest.MockConfig.class, ConsultRoomControllerTest.TestSecurityConfig.class})
class ConsultRoomControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConsultRoomService consultRoomService;

    @Autowired
    private ConsultRoomController consultRoomController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(consultRoomController)
                .build();
    }

    static class MockConfig {
        @Bean
        public ConsultRoomService consultRoomService(){
            return Mockito.mock(ConsultRoomService.class);
        }

        @Bean
        public JwtUtil jwtUtil() {
            return Mockito.mock(JwtUtil.class);
        }

        @Bean
        public UserInfoService userInfoService() {
            return Mockito.mock(UserInfoService.class);
        }

        @Bean
        public JwtFilter jwtFilter() {
            return Mockito.mock(JwtFilter.class);
        }
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    private ConsultRoomDTO createConsultRoomDTO() {
        return ConsultRoomDTO.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(1)
                .description("Urgencies consult room")
                .build();
    }

    @Test
    void shouldCreateConsultRoom() throws Exception {
        ConsultRoomDTO dto= createConsultRoomDTO();

        when(consultRoomService.createConsultRoom(any())).thenReturn(dto);

        mockMvc.perform(post("/api/ConsultRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.floor").value(1))
                .andExpect(jsonPath("$.name").value("Consult Room A"));
    }

    @Test
    void shouldGetAllConsultRooms() throws Exception {
        ConsultRoomDTO dto = createConsultRoomDTO();

        when(consultRoomService.getAllConsultRooms()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/ConsultRoom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Consult Room A"))
                .andExpect(jsonPath("$[0].floor").value(1))
                .andExpect(jsonPath("$[0].description").value("Urgencies consult room"));
    }

    @Test
    void shouldGetConsultRoomById() throws Exception{
        ConsultRoomDTO dto = createConsultRoomDTO();

        when(consultRoomService.getConsultRoomById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/ConsultRoom/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Consult Room A"))
                .andExpect(jsonPath("$.floor").value(1));
    }

    @Test
    void shouldUpdateConsultRoom() throws Exception{
        ConsultRoomDTO dto = createConsultRoomDTO();

        when(consultRoomService.updateConsultRoom(eq(1L), any())).thenReturn(dto);
        mockMvc.perform(put("/api/ConsultRoom/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Consult Room A"))
                .andExpect(jsonPath("$.description").value("Urgencies consult room"));
    }

    @Test
    void shouldDeleteConsultRoom() throws Exception{
        ConsultRoomDTO dto = createConsultRoomDTO();

        mockMvc.perform(delete("/api/ConsultRoom/1"))
                .andExpect(status().isNoContent());

        verify(consultRoomService).deleteConsultRoom(1L);
    }


  
}