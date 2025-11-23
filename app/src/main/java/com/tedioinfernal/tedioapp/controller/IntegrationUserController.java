package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.IntegrationUserRequestDTO;
import com.tedioinfernal.tedioapp.dto.UserResponseDTO;
import com.tedioinfernal.tedioapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/integration")
@RequiredArgsConstructor
@Slf4j
public class IntegrationUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createIntegrationUser(
            @Valid @RequestBody IntegrationUserRequestDTO requestDTO) {
        log.info("POST /api/user/integration - Creating integration user");
        UserResponseDTO response = userService.createIntegrationUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
