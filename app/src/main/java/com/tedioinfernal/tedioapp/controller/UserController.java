package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.ApplicationUserRequestDTO;
import com.tedioinfernal.tedioapp.dto.UserResponseDTO;
import com.tedioinfernal.tedioapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createApplicationUser(
            @Valid @RequestBody ApplicationUserRequestDTO requestDTO) {
        log.info("POST /api/user - Creating application user");
        UserResponseDTO response = userService.createApplicationUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email) {
        
        if (id != null) {
            log.info("GET /api/user?id={} - Fetching user by ID", id);
            UserResponseDTO response = userService.getUserById(id);
            return ResponseEntity.ok(response);
        }
        
        if (email != null && !email.isEmpty()) {
            log.info("GET /api/user?email={} - Fetching user by email", email);
            UserResponseDTO response = userService.getUserByEmail(email);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/user - Fetching all users");
        List<UserResponseDTO> response = userService.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUserRequestDTO requestDTO) {
        log.info("PUT /api/user/{} - Updating user", id);
        UserResponseDTO response = userService.updateUser(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /api/user/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
