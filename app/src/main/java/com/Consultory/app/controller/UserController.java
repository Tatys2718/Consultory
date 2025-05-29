package com.Consultory.app.controller;

import com.Consultory.app.dto.AuthRequest;
import com.Consultory.app.dto.UserInfo;
import com.Consultory.app.model.ERol;
import com.Consultory.app.security.jwt.JwtUtil;
import com.Consultory.app.security.services.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class UserController {
    private final UserInfoService service;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(authRequest.username());
            return ResponseEntity.ok(token);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<UserInfo> addNewUser(@RequestBody UserInfo userInfo) {
        if (!userInfo.roles().contains(ERol.ROLE_ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo se pueden registrar usuarios con rol ADMIN");
        }

        UserInfo response = service.addUser(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
