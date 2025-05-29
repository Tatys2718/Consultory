package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.DoctorRepository;
import com.Consultory.app.Repository.RoleRepository;
import com.Consultory.app.Repository.UserRepository;
import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.exception.ResourceNotFoundException;
import com.Consultory.app.mapper.DoctorMapper;
import com.Consultory.app.model.*;
import com.Consultory.app.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;

    public DoctorDTO createDoctor(DoctorDTO dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getId()));

        Doctor doctor = doctorMapper.toEntity(dto);
        doctor.setUser(user);
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }
    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctorMapper::toDto).toList();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        return doctorRepository.findById(id).map(doctorMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
    }
    @Override
    public List<DoctorDTO> getDoctorsBySpecialty(String speciality){
        List<Doctor> doctors = doctorRepository.findBySpecialityIgnoreCase(speciality);
        return doctors.stream().map(doctorMapper::toDto).toList();
    }
    @Override
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        existing.setFullName(doctorDTO.getFullName());
        existing.setEmail(doctorDTO.getEmail());
        existing.setSpeciality(doctorDTO.getSpeciality());
        existing.setAvailableFrom(doctorDTO.getAvailableFrom());
        existing.setAvailableTo(doctorDTO.getAvailableTo());
        return doctorMapper.toDto(doctorRepository.save(existing));
    }

    @Override
    public void deleteDoctor(Long id) {
        if(!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }
}
