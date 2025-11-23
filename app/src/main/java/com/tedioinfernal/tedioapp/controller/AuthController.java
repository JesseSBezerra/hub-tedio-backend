package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.AuthResponseDTO;
import com.tedioinfernal.tedioapp.dto.LoginRequestDTO;
import com.tedioinfernal.tedioapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("POST /api/auth/login - User login attempt");
        AuthResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
