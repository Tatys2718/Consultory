package com.Consultory.app.controller;

import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.dto.PatientDTO;
import com.Consultory.app.security.jwt.JwtFilter;
import com.Consultory.app.security.jwt.JwtUtil;
import com.Consultory.app.security.services.UserInfoService;
import com.Consultory.app.service.AppointmentService;
import com.Consultory.app.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@Import(DoctorControllerTest.MockConfig.class)
class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DoctorService doctorService;
    @Autowired private DoctorController doctorController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController)
                .build();
    }

    static class MockConfig {
        @Bean
        public DoctorService doctorService(){
            return Mockito.mock(DoctorService.class);
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


    private DoctorDTO createDoctorDTO() {
        return DoctorDTO.builder()
                .id(1L)
                .userId(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .speciality("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build();
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        DoctorDTO dto= createDoctorDTO();

        when(doctorService.createDoctor(any())).thenReturn(dto);

        mockMvc.perform(post("/api/Doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$.email").value("pgonzalez@gmail.com"))
                .andExpect(jsonPath("$.speciality").value("Cardiology"));
    }

    @Test
    void shouldGetAllDoctors() throws Exception {
        DoctorDTO dto = createDoctorDTO();

        when(doctorService.getAllDoctors()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/Doctor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$[0].email").value("pgonzalez@gmail.com"))
                .andExpect(jsonPath("$[0].speciality").value("Cardiology"));

    }

    @Test
    void shouldGetDoctorById() throws Exception {
        DoctorDTO dto = createDoctorDTO();

        when(doctorService.getDoctorById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/Doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Pedro Gonzalez"));
    }

    @Test
    void shouldDeleteDoctor() throws Exception{
        DoctorDTO dto = createDoctorDTO();

        mockMvc.perform(delete("/api/Doctor/1"))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteDoctor(1L);
    }

}
