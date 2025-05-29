package com.Consultory.app.controller;

import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.dto.PatientDTO;
import com.Consultory.app.model.Patient;
import com.Consultory.app.security.jwt.JwtFilter;
import com.Consultory.app.security.jwt.JwtUtil;
import com.Consultory.app.security.services.UserInfoService;
import com.Consultory.app.service.AppointmentService;
import com.Consultory.app.service.PatientService;
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
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PatientController.class)
@Import(PatientControllerTest.MockConfig.class)
class PatientControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
                .build();
    }

    static class MockConfig {
        @Bean
        public PatientService patientService(){
            return Mockito.mock(PatientService.class);
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

    private PatientDTO createPatientDTO() {
        return PatientDTO.builder()
                .id(1L)
                .fullName("Teresa Chavez")
                .email("teresaCha4@gmail.com")
                .phone("3134129962")
                .build();
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientDTO dto= createPatientDTO();

        when(patientService.createPatient(any())).thenReturn(dto);

        mockMvc.perform(post("/api/Patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Teresa Chavez"))
                .andExpect(jsonPath("$.email").value("teresaCha4@gmail.com"))
                .andExpect(jsonPath("$.phone").value("3134129962"));
    }


    @Test
    void shouldGetAllPatients() throws Exception {
        PatientDTO dto = createPatientDTO() ;

        when(patientService.getAllPatients()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/Patient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Teresa Chavez"))
                .andExpect(jsonPath("$[0].email").value("teresaCha4@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("3134129962"));

    }

    @Test
    void shouldGetPatientById() throws Exception {
        PatientDTO dto = createPatientDTO();

        when(patientService.getPatientById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/Patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Teresa Chavez"));
    }

    @Test
    void shouldUpdatePatientRoom() throws Exception{
        PatientDTO dto = createPatientDTO();

        when(patientService.updatePatient(eq(1L), any())).thenReturn(dto);
        mockMvc.perform(put("/api/Patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Teresa Chavez"))
                .andExpect(jsonPath("$.phone").value("3134129962"));
    }

    @Test
    void shouldDeletePatient() throws Exception{
        PatientDTO dto = createPatientDTO();

        mockMvc.perform(delete("/api/Patient/1"))
                .andExpect(status().isNoContent());

        verify(patientService).deletePatient(1L);
    }

}