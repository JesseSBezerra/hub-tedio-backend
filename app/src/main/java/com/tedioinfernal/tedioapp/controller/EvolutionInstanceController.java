package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.EvolutionInstanceRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionInstanceResponseDTO;
import com.tedioinfernal.tedioapp.dto.SetWebhookInstanceRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.LogoutResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.ConnectionStateDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.ConnectResponseDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.EvolutionInstanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evolution-instance")
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceController {

    private final EvolutionInstanceService evolutionInstanceService;

    @PostMapping
    public ResponseEntity<EvolutionInstanceResponseDTO> createInstance(
            @Valid @RequestBody EvolutionInstanceRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution-instance - Creating instance by user ID: {}", currentUser.getId());
        
        EvolutionInstanceResponseDTO response = evolutionInstanceService.createInstance(requestDTO, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvolutionInstanceResponseDTO> getInstanceById(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/evolution-instance/{} - Fetching instance by user ID: {}", id, currentUser.getId());
        
        EvolutionInstanceResponseDTO response = evolutionInstanceService.getInstanceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{instanceName}")
    public ResponseEntity<EvolutionInstanceResponseDTO> getInstanceByName(@PathVariable String instanceName) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/evolution-instance/name/{} - Fetching instance by user ID: {}", 
                instanceName, currentUser.getId());
        
        EvolutionInstanceResponseDTO response = evolutionInstanceService.getInstanceByName(instanceName);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EvolutionInstanceResponseDTO>> getAllInstances(
            @RequestParam(required = false) Long evolutionId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status) {
        
        User currentUser = UserContext.getCurrentUser();
        
        if (evolutionId != null) {
            log.info("GET /api/evolution-instance?evolutionId={} - Fetching instances by user ID: {}", 
                    evolutionId, currentUser.getId());
            List<EvolutionInstanceResponseDTO> response = 
                    evolutionInstanceService.getInstancesByEvolutionId(evolutionId);
            return ResponseEntity.ok(response);
        }
        
        if (userId != null) {
            log.info("GET /api/evolution-instance?userId={} - Fetching instances by user ID: {}", 
                    userId, currentUser.getId());
            List<EvolutionInstanceResponseDTO> response = 
                    evolutionInstanceService.getInstancesByUserId(userId);
            return ResponseEntity.ok(response);
        }
        
        if (status != null) {
            log.info("GET /api/evolution-instance?status={} - Fetching instances by user ID: {}", 
                    status, currentUser.getId());
            List<EvolutionInstanceResponseDTO> response = 
                    evolutionInstanceService.getInstancesByStatus(status);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/evolution-instance - Fetching all instances by user ID: {}", currentUser.getId());
        List<EvolutionInstanceResponseDTO> response = evolutionInstanceService.getAllInstances();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/connect")
    public ResponseEntity<ConnectResponseDTO> connectInstance(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/evolution-instance/{}/connect - Connecting instance by user ID: {}", id, currentUser.getId());
        
        ConnectResponseDTO response = evolutionInstanceService.connectInstance(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/connection-state")
    public ResponseEntity<ConnectionStateDTO> getConnectionState(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/evolution-instance/{}/connection-state - Getting connection state by user ID: {}", id, currentUser.getId());
        
        ConnectionStateDTO response = evolutionInstanceService.getConnectionState(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/webhook")
    public ResponseEntity<EvolutionInstanceResponseDTO> setWebhook(
            @PathVariable Long id,
            @Valid @RequestBody SetWebhookInstanceRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution-instance/{}/webhook - Setting webhook by user ID: {} - URL: {}", 
                id, currentUser.getId(), requestDTO.getWebhookUrl());
        
        EvolutionInstanceResponseDTO response = evolutionInstanceService.setWebhook(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/logout")
    public ResponseEntity<LogoutResponseDTO> logoutInstance(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution-instance/{}/logout - Logging out instance by user ID: {}", id, currentUser.getId());
        
        LogoutResponseDTO response = evolutionInstanceService.logoutInstance(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstance(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("DELETE /api/evolution-instance/{} - Deleting instance by user ID: {}", id, currentUser.getId());
        
        evolutionInstanceService.deleteInstance(id);
        return ResponseEntity.noContent().build();
    }
}
