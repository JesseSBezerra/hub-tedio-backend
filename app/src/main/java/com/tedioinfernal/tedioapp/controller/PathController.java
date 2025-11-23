package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.PathRequestDTO;
import com.tedioinfernal.tedioapp.dto.PathResponseDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.PathService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/path")
@RequiredArgsConstructor
@Slf4j
public class PathController {

    private final PathService pathService;

    @PostMapping
    public ResponseEntity<PathResponseDTO> createPath(
            @Valid @RequestBody PathRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/path - Creating path by user ID: {}", currentUser.getId());
        
        PathResponseDTO response = pathService.createPath(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PathResponseDTO> getPathById(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/path/{} - Fetching path by user ID: {}", id, currentUser.getId());
        
        PathResponseDTO response = pathService.getPathById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PathResponseDTO>> getAllPaths(
            @RequestParam(required = false) Long integrationId) {
        
        User currentUser = UserContext.getCurrentUser();
        
        if (integrationId != null) {
            log.info("GET /api/path?integrationId={} - Fetching paths by integration, user ID: {}", integrationId, currentUser.getId());
            List<PathResponseDTO> response = pathService.getPathsByIntegrationId(integrationId);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/path - Fetching all paths by user ID: {}", currentUser.getId());
        List<PathResponseDTO> response = pathService.getAllPaths();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PathResponseDTO> updatePath(
            @PathVariable Long id,
            @Valid @RequestBody PathRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("PUT /api/path/{} - Updating path by user ID: {}", id, currentUser.getId());
        
        PathResponseDTO response = pathService.updatePath(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePath(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("DELETE /api/path/{} - Deleting path by user ID: {}", id, currentUser.getId());
        
        pathService.deletePath(id);
        return ResponseEntity.noContent().build();
    }
}
