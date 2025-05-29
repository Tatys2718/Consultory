package com.Consultory.app.controller;

import com.Consultory.app.dto.ConsultRoomDTO;
import com.Consultory.app.dto.MedicalRecordDTO;
import com.Consultory.app.security.jwt.JwtFilter;
import com.Consultory.app.security.jwt.JwtUtil;
import com.Consultory.app.security.services.UserInfoService;
import com.Consultory.app.service.AppointmentService;
import com.Consultory.app.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
@Import(MedicalRecordControllerTest.MockConfig.class)
class MedicalRecordControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicalRecordController)
                .build();
    }

    static class MockConfig {
        @Bean
        public MedicalRecordService medicalRecordService(){
            return Mockito.mock(MedicalRecordService.class);
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

    private MedicalRecordDTO medicalRecordDTO() {
        return MedicalRecordDTO.builder()
                .id(1L)
                .patientId(1L)
                .appointmentId(1L)
                .diagnosis("Patient is well")
                .notes("Patient has no complaints")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldCreateMedicalRecord() throws Exception{
        MedicalRecordDTO dto = medicalRecordDTO();

        when(medicalRecordService.createMedicalRecord(any())).thenReturn(dto);

        mockMvc.perform(post("/api/MedicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.appointmentId").value(1L))
                .andExpect(jsonPath("$.diagnosis").value("Patient is well"));
    }

    @Test
     void shouldGetAllMedicalRecords() throws Exception{
        MedicalRecordDTO dto = medicalRecordDTO();

        when(medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/MedicalRecord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnosis").value("Patient is well"));
    }

    @Test
    void shouldGetMedicalRecordById() throws Exception{
        MedicalRecordDTO dto = medicalRecordDTO();

        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/MedicalRecord/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Patient is well"))
                .andExpect(jsonPath("$.notes").value("Patient has no complaints"))
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.appointmentId").value(1L));
    }

    @Test
    void shouldGetMedicalRecordByPatientId() throws Exception{
        MedicalRecordDTO dto = medicalRecordDTO();

        when(medicalRecordService.getMedicalRecordsByPatientId(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/MedicalRecord/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L))
                .andExpect(jsonPath("$[0].diagnosis").value("Patient is well"));
    }

    @Test
    void shouldUpdateMedicalRecord() throws Exception{
        MedicalRecordDTO dto = medicalRecordDTO();

        when(medicalRecordService.updateMedicalRecord(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/MedicalRecord/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.appointmentId").value(1L))
                .andExpect(jsonPath("$.diagnosis").value("Patient is well"))
                .andExpect(jsonPath("$.notes").value("Patient has no complaints"));

    }

    @Test
    void shouldDeleteMedicalRecord() throws Exception {
        MedicalRecordDTO dto = MedicalRecordDTO.builder()
                .id(1L)
                .patientId(1L)
                .appointmentId(1L)
                .diagnosis("Patient is well")
                .notes("Patient has no complaints")
                .created(LocalDateTime.now())
                .build();

        mockMvc.perform(delete("/api/MedicalRecord/1"))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).deleteMedicalRecord(1L);
    }




}