package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.EvolutionMediaRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionMediaResponseDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.EvolutionMediaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evolution/media")
@RequiredArgsConstructor
@Slf4j
public class EvolutionMediaController {

    private final EvolutionMediaService evolutionMediaService;

    @PostMapping
    public ResponseEntity<EvolutionMediaResponseDTO> getBase64FromMediaMessage(
            @Valid @RequestBody EvolutionMediaRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution/media - Getting media base64 by user ID: {}", currentUser.getId());
        
        EvolutionMediaResponseDTO response = evolutionMediaService.getBase64FromMediaMessage(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
