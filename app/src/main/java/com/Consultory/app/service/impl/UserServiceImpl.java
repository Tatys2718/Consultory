package com.Consultory.app.service.impl;

import com.Consultory.app.Repository.UserRepository;

import com.Consultory.app.dto.UserDTO;
import com.Consultory.app.exception.ResourceNotFoundException;
import com.Consultory.app.mapper.UserMapper;
import com.Consultory.app.model.User;
import com.Consultory.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).toList();
    }
    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        existing.setUsername(userDTO.getUsername());
        existing.setEmail(userDTO.getEmail());
        return userMapper.toDTO(userRepository.save(existing));
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
