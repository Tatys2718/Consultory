package com.Consultory.app.service;

import com.Consultory.app.dto.UserDTO;
import com.Consultory.app.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO dto);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO dto);
    void deleteUser(Long id);
}
