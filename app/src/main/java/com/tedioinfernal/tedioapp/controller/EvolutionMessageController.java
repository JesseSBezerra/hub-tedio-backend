package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.EvolutionMessageRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionMessageResponseDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionMediaMessageRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionAudioMessageRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionStickerMessageRequestDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.EvolutionMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evolution/message")
@RequiredArgsConstructor
@Slf4j
public class EvolutionMessageController {

    private final EvolutionMessageService evolutionMessageService;

    @PostMapping
    public ResponseEntity<EvolutionMessageResponseDTO> sendMessage(
            @Valid @RequestBody EvolutionMessageRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution/message - Sending message by user ID: {}", currentUser.getId());
        
        EvolutionMessageResponseDTO response = evolutionMessageService.sendMessage(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/media")
    public ResponseEntity<EvolutionMessageResponseDTO> sendMedia(
            @Valid @RequestBody EvolutionMediaMessageRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution/message/media - Sending media by user ID: {}", currentUser.getId());
        
        EvolutionMessageResponseDTO response = evolutionMessageService.sendMedia(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/audio")
    public ResponseEntity<EvolutionMessageResponseDTO> sendAudio(
            @Valid @RequestBody EvolutionAudioMessageRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution/message/audio - Sending audio by user ID: {}", currentUser.getId());
        
        EvolutionMessageResponseDTO response = evolutionMessageService.sendAudio(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sticker")
    public ResponseEntity<EvolutionMessageResponseDTO> sendSticker(
            @Valid @RequestBody EvolutionStickerMessageRequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/evolution/message/sticker - Sending sticker by user ID: {}", currentUser.getId());
        
        EvolutionMessageResponseDTO response = evolutionMessageService.sendSticker(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
