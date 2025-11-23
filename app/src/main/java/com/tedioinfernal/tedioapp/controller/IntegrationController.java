package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.IntegrationRequestDTO;
import com.tedioinfernal.tedioapp.dto.IntegrationResponseDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.IntegrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
@Slf4j
public class IntegrationController {

    private final IntegrationService integrationService;

    @PostMapping
    public ResponseEntity<IntegrationResponseDTO> createIntegration(
            @Valid @RequestBody IntegrationRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/integration - Creating integration by user ID: {}", currentUser.getId());
        
        IntegrationResponseDTO response = integrationService.createIntegration(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IntegrationResponseDTO> getIntegrationById(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/integration/{} - Fetching integration by user ID: {}", id, currentUser.getId());
        
        IntegrationResponseDTO response = integrationService.getIntegrationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IntegrationResponseDTO>> getAllIntegrations(
            @RequestParam(required = false) Long ownerId) {
        
        User currentUser = UserContext.getCurrentUser();
        
        if (ownerId != null) {
            log.info("GET /api/integration?ownerId={} - Fetching integrations by owner, user ID: {}", ownerId, currentUser.getId());
            List<IntegrationResponseDTO> response = integrationService.getIntegrationsByOwnerId(ownerId);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/integration - Fetching all integrations by user ID: {}", currentUser.getId());
        List<IntegrationResponseDTO> response = integrationService.getAllIntegrations();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntegrationResponseDTO> updateIntegration(
            @PathVariable Long id,
            @Valid @RequestBody IntegrationRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("PUT /api/integration/{} - Updating integration by user ID: {}", id, currentUser.getId());
        
        IntegrationResponseDTO response = integrationService.updateIntegration(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntegration(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("DELETE /api/integration/{} - Deleting integration by user ID: {}", id, currentUser.getId());
        
        integrationService.deleteIntegration(id);
        return ResponseEntity.noContent().build();
    }
}
