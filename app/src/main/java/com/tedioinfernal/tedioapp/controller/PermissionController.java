package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.PermissionRequestDTO;
import com.tedioinfernal.tedioapp.dto.UserPermissionsResponseDTO;
import com.tedioinfernal.tedioapp.entity.Permission;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<UserPermissionsResponseDTO> assignPermissions(
            @Valid @RequestBody PermissionRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        
        log.info("POST /api/permission - Assigning permissions to user ID: {}", currentUser.getId());
        
        UserPermissionsResponseDTO response = permissionService.assignPermissionsToUser(
                currentUser.getId(), 
                requestDTO.getPermissions()
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<UserPermissionsResponseDTO> getUserPermissions() {
        
        User currentUser = UserContext.getCurrentUser();
        
        log.info("GET /api/permission - Fetching permissions for user ID: {}", currentUser.getId());
        
        UserPermissionsResponseDTO response = permissionService.getUserPermissions(currentUser.getId());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Permission>> getAvailablePermissions() {
        log.info("GET /api/permission/available - Fetching all available permissions");
        
        List<Permission> permissions = permissionService.getAllPermissions();
        
        return ResponseEntity.ok(permissions);
    }
}
