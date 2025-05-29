package com.Consultory.app.controller;

import com.Consultory.app.dto.AppointmentDTO;
import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.model.AppointmentStatus;
import com.Consultory.app.security.jwt.JwtFilter;
import com.Consultory.app.security.jwt.JwtUtil;
import com.Consultory.app.security.services.UserInfoService;
import com.Consultory.app.service.AppointmentService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import({AppointmentControllerTest.MockConfig.class, AppointmentControllerTest.TestSecurityConfig.class})
class AppointmentControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController)
                .build();
    }

    static class MockConfig {
        @Bean
        public AppointmentService appointmentService(){
            return Mockito.mock(AppointmentService.class);
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

    private AppointmentDTO createAppoinmentDTO() {
        return AppointmentDTO.builder()
                .id(1L)
                .patientId(1L)
                .consultRoomId(1L)
                .doctorId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    @Test
    void shouldCreateAppointment() throws Exception{
        AppointmentDTO dto= createAppoinmentDTO();

        when(appointmentService.createAppointment(any())).thenReturn(dto);

        mockMvc.perform(post("/api/Appointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.consultRoomId").value(1L));

    }

    @Test
    void shouldGetAllAppoinments() throws Exception{
        AppointmentDTO dto= createAppoinmentDTO();

        when(appointmentService.getAllAppointments()).thenReturn(List.of(dto));
        mockMvc.perform(get("/api/Appointment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L))
                .andExpect(jsonPath("$[0].doctorId").value(1L))
                .andExpect(jsonPath("$[0].consultRoomId").value(1L));
    }

    @Test
    void shouldGetAppointmentById() throws Exception{
        AppointmentDTO dto= createAppoinmentDTO();

        when(appointmentService.getAppointmentById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/Appointment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.consultRoomId").value(1L));
    }

    @Test
    void shouldUpdateAppointment() throws Exception{
        AppointmentDTO dto= createAppoinmentDTO();

        when(appointmentService.updateAppointment(eq(1L), any())).thenReturn(dto);
        mockMvc.perform(put("/api/Appointment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.consultRoomId").value(1L));
    }

    @Test
    void shouldCancelAppointment() throws Exception{
        AppointmentDTO dto = AppointmentDTO.builder()
                .id(1L)
                .patientId(1L)
                .consultRoomId(1L)
                .doctorId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.CANCELED)
                .build();

        when(appointmentService.cancelAppointment(eq(1L))).thenReturn(dto);

        mockMvc.perform(delete("/api/Appointment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));
        verify(appointmentService).cancelAppointment(1L);
    }
}
