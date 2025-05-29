package com.Consultory.app.security.services;

import com.Consultory.app.Repository.RoleRepository;
import com.Consultory.app.Repository.UserRepository;
import com.Consultory.app.dto.UserInfo;
import com.Consultory.app.model.ERol;
import com.Consultory.app.model.Role;
import com.Consultory.app.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserInfoService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
        return new UserInfoDetail(user);
    }
    public UserInfo addUser(UserInfo userInfo){
        Set<Role> roles = userInfo.roles().stream()
                .map(rolEnum -> roleRepository.findByName(rolEnum)
                        .orElseThrow(() -> new RuntimeException("Role no encontrado: " + rolEnum)))
                .collect(Collectors.toSet());

        User user = new User();
        user.setUsername(userInfo.username());
        user.setPassword(passwordEncoder.encode(userInfo.password()));
        user.setEmail(userInfo.email());
        user.setRoles(roles);
        user = userRepository.save(user);
        Set<ERol> roleEnums = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserInfo(user.getUsername(), userInfo.password(), user.getEmail(), roleEnums);
    }

}
