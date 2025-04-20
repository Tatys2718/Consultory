package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.PatientRepository;
import com.Consultory.app.dto.PatientDTO;
import com.Consultory.app.mapper.PatientMapper;
import com.Consultory.app.model.Patient;
import com.Consultory.app.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = patientMapper.toEntity(patientDTO);
        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toDTO).toList();
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
        existing.setFullName(patientDTO.getFullName());
        existing.setEmail(patientDTO.getEmail());
        existing.setPhone(patientDTO.getPhone());
        return patientMapper.toDTO(patientRepository.save(existing));
    }

    @Override
    public void deletePatient(Long id) {
        if(!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }
}
