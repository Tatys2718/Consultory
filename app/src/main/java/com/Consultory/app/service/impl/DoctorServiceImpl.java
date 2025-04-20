package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.DoctorRepository;
import com.Consultory.app.dto.DoctorDTO;
import com.Consultory.app.mapper.DoctorMapper;
import com.Consultory.app.model.Doctor;
import com.Consultory.app.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl extends DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorDTO createDoctor(DoctorDTO dto){
        Doctor doctor = doctorMapper.toEntity(dto);
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
    public List<DoctorDTO> getDoctorsBySpecialty(String specialty){
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
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
