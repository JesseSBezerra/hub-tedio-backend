package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.EvolutionMessageRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionMessageResponseDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionMediaMessageRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionAudioMessageRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionStickerMessageRequestDTO;
import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.SendTextRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.SendTextResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.service.EvolutionTextMessageService;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto.SendMediaRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto.SendMediaResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.service.EvolutionMediaMessageService;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto.SendAudioRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto.SendAudioResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.service.EvolutionAudioMessageService;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto.SendStickerRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto.SendStickerResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.service.EvolutionStickerMessageService;
import com.tedioinfernal.tedioapp.repository.EvolutionInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionMessageService {

    private final EvolutionInstanceRepository evolutionInstanceRepository;
    private final EvolutionTextMessageService evolutionTextMessageService;
    private final EvolutionMediaMessageService evolutionMediaMessageService;
    private final EvolutionAudioMessageService evolutionAudioMessageService;
    private final EvolutionStickerMessageService evolutionStickerMessageService;

    /**
     * Envia mensagem de texto via Evolution
     * 1. Busca EvolutionInstance
     * 2. Chama integração Evolution para enviar mensagem
     * 3. Retorna response simplificado
     */
    @Transactional(readOnly = true)
    public EvolutionMessageResponseDTO sendMessage(EvolutionMessageRequestDTO requestDTO) {
        
        log.info("Sending message to {} via Evolution Instance ID: {}", 
                requestDTO.getNumber(), requestDTO.getEvolutionInstanceId());

        // Busca EvolutionInstance configurada
        EvolutionInstance evolutionInstance = evolutionInstanceRepository
                .findById(requestDTO.getEvolutionInstanceId())
                .orElseThrow(() -> new RuntimeException(
                        "Evolution Instance não encontrada com ID: " + requestDTO.getEvolutionInstanceId()));

        log.info("Using Evolution Instance: {} (Evolution: {})", 
                evolutionInstance.getInstanceName(),
                evolutionInstance.getEvolution().getNome());

        // Prepara request para integração
        SendTextRequestDTO integrationRequest = SendTextRequestDTO.builder()
                .number(requestDTO.getNumber())
                .text(requestDTO.getMessage())
                .build();

        try {
            // Chama integração Evolution para enviar mensagem
            log.info("Calling Evolution API to send message...");
            SendTextResponseDTO integrationResponse = 
                    evolutionTextMessageService.sendText(evolutionInstance, integrationRequest);

            log.info("Message sent successfully. Message ID: {}, Status: {}", 
                    integrationResponse.getKey().getId(),
                    integrationResponse.getStatus());

            // Retorna response simplificado
            return EvolutionMessageResponseDTO.builder()
                    .number(requestDTO.getNumber())
                    .message(requestDTO.getMessage())
                    .status(integrationResponse.getStatus())
                    .build();

        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de mídia via Evolution
     */
    @Transactional(readOnly = true)
    public EvolutionMessageResponseDTO sendMedia(EvolutionMediaMessageRequestDTO requestDTO) {
        
        log.info("Sending media ({}) to {} via Evolution Instance ID: {}", 
                requestDTO.getMediatype(), requestDTO.getNumber(), requestDTO.getEvolutionInstanceId());

        EvolutionInstance evolutionInstance = evolutionInstanceRepository
                .findById(requestDTO.getEvolutionInstanceId())
                .orElseThrow(() -> new RuntimeException(
                        "Evolution Instance não encontrada com ID: " + requestDTO.getEvolutionInstanceId()));

        log.info("Using Evolution Instance: {} (Evolution: {})", 
                evolutionInstance.getInstanceName(),
                evolutionInstance.getEvolution().getNome());

        SendMediaRequestDTO integrationRequest = SendMediaRequestDTO.builder()
                .number(requestDTO.getNumber())
                .mediatype(requestDTO.getMediatype())
                .mimetype(requestDTO.getMimetype())
                .media(requestDTO.getMedia())
                .fileName(requestDTO.getFileName())
                .build();

        try {
            log.info("Calling Evolution API to send media...");
            SendMediaResponseDTO integrationResponse = 
                    evolutionMediaMessageService.sendMedia(evolutionInstance, integrationRequest);

            log.info("Media sent successfully. Message ID: {}, Status: {}", 
                    integrationResponse.getKey().getId(),
                    integrationResponse.getStatus());

            return EvolutionMessageResponseDTO.builder()
                    .number(requestDTO.getNumber())
                    .message(requestDTO.getMediatype() + " - " + requestDTO.getFileName())
                    .status(integrationResponse.getStatus())
                    .build();

        } catch (Exception e) {
            log.error("Error sending media: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mídia: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de áudio via Evolution
     */
    @Transactional(readOnly = true)
    public EvolutionMessageResponseDTO sendAudio(EvolutionAudioMessageRequestDTO requestDTO) {
        
        log.info("Sending audio to {} via Evolution Instance ID: {}", 
                requestDTO.getNumber(), requestDTO.getEvolutionInstanceId());

        EvolutionInstance evolutionInstance = evolutionInstanceRepository
                .findById(requestDTO.getEvolutionInstanceId())
                .orElseThrow(() -> new RuntimeException(
                        "Evolution Instance não encontrada com ID: " + requestDTO.getEvolutionInstanceId()));

        log.info("Using Evolution Instance: {} (Evolution: {})", 
                evolutionInstance.getInstanceName(),
                evolutionInstance.getEvolution().getNome());

        SendAudioRequestDTO integrationRequest = SendAudioRequestDTO.builder()
                .number(requestDTO.getNumber())
                .audio(requestDTO.getAudio())
                .build();

        try {
            log.info("Calling Evolution API to send audio...");
            SendAudioResponseDTO integrationResponse = 
                    evolutionAudioMessageService.sendAudio(evolutionInstance, integrationRequest);

            log.info("Audio sent successfully. Message ID: {}, Status: {}", 
                    integrationResponse.getKey().getId(),
                    integrationResponse.getStatus());

            return EvolutionMessageResponseDTO.builder()
                    .number(requestDTO.getNumber())
                    .message("Audio message")
                    .status(integrationResponse.getStatus())
                    .build();

        } catch (Exception e) {
            log.error("Error sending audio: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar áudio: " + e.getMessage(), e);
        }
    }

    /**
     * Envia sticker via Evolution
     */
    @Transactional(readOnly = true)
    public EvolutionMessageResponseDTO sendSticker(EvolutionStickerMessageRequestDTO requestDTO) {
        
        log.info("Sending sticker to {} via Evolution Instance ID: {}", 
                requestDTO.getNumber(), requestDTO.getEvolutionInstanceId());

        EvolutionInstance evolutionInstance = evolutionInstanceRepository
                .findById(requestDTO.getEvolutionInstanceId())
                .orElseThrow(() -> new RuntimeException(
                        "Evolution Instance não encontrada com ID: " + requestDTO.getEvolutionInstanceId()));

        log.info("Using Evolution Instance: {} (Evolution: {})", 
                evolutionInstance.getInstanceName(),
                evolutionInstance.getEvolution().getNome());

        SendStickerRequestDTO integrationRequest = SendStickerRequestDTO.builder()
                .number(requestDTO.getNumber())
                .sticker(requestDTO.getSticker())
                .build();

        try {
            log.info("Calling Evolution API to send sticker...");
            SendStickerResponseDTO integrationResponse = 
                    evolutionStickerMessageService.sendSticker(evolutionInstance, integrationRequest);

            log.info("Sticker sent successfully. Message ID: {}, Status: {}", 
                    integrationResponse.getKey().getId(),
                    integrationResponse.getStatus());

            return EvolutionMessageResponseDTO.builder()
                    .number(requestDTO.getNumber())
                    .message("Sticker message")
                    .status(integrationResponse.getStatus())
                    .build();

        } catch (Exception e) {
            log.error("Error sending sticker: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar sticker: " + e.getMessage(), e);
        }
    }
}
