package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.EvolutionRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionResponseDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.EvolutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evolution")
@RequiredArgsConstructor
@Slf4j
public class EvolutionController {

    private final EvolutionService evolutionService;

    @PostMapping
    public ResponseEntity<EvolutionResponseDTO> createEvolution(
            @Valid @RequestBody EvolutionRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution - Creating evolution by user ID: {}", currentUser.getId());
        
        EvolutionResponseDTO response = evolutionService.createEvolution(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvolutionResponseDTO> getEvolutionById(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/evolution/{} - Fetching evolution by user ID: {}", id, currentUser.getId());
        
        EvolutionResponseDTO response = evolutionService.getEvolutionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<EvolutionResponseDTO> getEvolutionByNome(@PathVariable String nome) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/evolution/nome/{} - Fetching evolution by user ID: {}", nome, currentUser.getId());
        
        EvolutionResponseDTO response = evolutionService.getEvolutionByNome(nome);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EvolutionResponseDTO>> getAllEvolutions(
            @RequestParam(required = false) Long ownerId) {
        
        User currentUser = UserContext.getCurrentUser();
        
        if (ownerId != null) {
            log.info("GET /api/evolution?ownerId={} - Fetching evolutions by owner, user ID: {}", ownerId, currentUser.getId());
            List<EvolutionResponseDTO> response = evolutionService.getEvolutionsByOwnerId(ownerId);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/evolution - Fetching all evolutions by user ID: {}", currentUser.getId());
        List<EvolutionResponseDTO> response = evolutionService.getAllEvolutions();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvolutionResponseDTO> updateEvolution(
            @PathVariable Long id,
            @Valid @RequestBody EvolutionRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("PUT /api/evolution/{} - Updating evolution by user ID: {}", id, currentUser.getId());
        
        EvolutionResponseDTO response = evolutionService.updateEvolution(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvolution(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("DELETE /api/evolution/{} - Deleting evolution by user ID: {}", id, currentUser.getId());
        
        evolutionService.deleteEvolution(id);
        return ResponseEntity.noContent().build();
    }
}
