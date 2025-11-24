package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.EvolutionMediaRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionMediaResponseDTO;
import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.GetMediaBase64RequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.GetMediaBase64ResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.MessageDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.MessageKeyDTO;
import com.tedioinfernal.tedioapp.repository.EvolutionInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionMediaService {

    private final EvolutionInstanceRepository evolutionInstanceRepository;
    private final com.tedioinfernal.tedioapp.integrations.evolution.media.service.EvolutionMediaIntegrationService evolutionMediaIntegrationService;

    /**
     * Busca base64 de mensagem de mídia via Evolution
     * 1. Busca EvolutionInstance
     * 2. Chama integração Evolution para buscar base64
     * 3. Retorna response simplificado
     */
    @Transactional(readOnly = true)
    public EvolutionMediaResponseDTO getBase64FromMediaMessage(EvolutionMediaRequestDTO requestDTO) {
        
        log.info("Getting base64 from media message ID: {} via Evolution Instance ID: {}", 
                requestDTO.getMessageId(), requestDTO.getEvolutionInstanceId());

        // Busca EvolutionInstance configurada
        EvolutionInstance evolutionInstance = evolutionInstanceRepository
                .findById(requestDTO.getEvolutionInstanceId())
                .orElseThrow(() -> new RuntimeException(
                        "Evolution Instance não encontrada com ID: " + requestDTO.getEvolutionInstanceId()));

        log.info("Using Evolution Instance: {} (Evolution: {})", 
                evolutionInstance.getInstanceName(),
                evolutionInstance.getEvolution().getNome());

        // Prepara request para integração
        MessageKeyDTO messageKey = MessageKeyDTO.builder()
                .id(requestDTO.getMessageId())
                .build();
        
        MessageDTO message = MessageDTO.builder()
                .key(messageKey)
                .build();
        
        GetMediaBase64RequestDTO integrationRequest = GetMediaBase64RequestDTO.builder()
                .message(message)
                .convertToMp4(requestDTO.getConvertToMp4())
                .build();

        try {
            // Chama integração Evolution para buscar base64
            log.info("Calling Evolution API to get media base64...");
            GetMediaBase64ResponseDTO integrationResponse = 
                    evolutionMediaIntegrationService.getBase64FromMediaMessage(evolutionInstance, integrationRequest);

            log.info("Media base64 retrieved successfully. Media Type: {}, File: {}, Size: {} bytes", 
                    integrationResponse.getMediaType(),
                    integrationResponse.getFileName(),
                    integrationResponse.getSize() != null ? integrationResponse.getSize().getFileLength() : "unknown");

            // Retorna response simplificado
            return EvolutionMediaResponseDTO.builder()
                    .mediaType(integrationResponse.getMediaType())
                    .fileName(integrationResponse.getFileName())
                    .fileLength(integrationResponse.getSize() != null ? integrationResponse.getSize().getFileLength() : null)
                    .mimetype(integrationResponse.getMimetype())
                    .base64(integrationResponse.getBase64())
                    .build();

        } catch (Exception e) {
            log.error("Error getting media base64: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao buscar base64 da mensagem de mídia: " + e.getMessage(), e);
        }
    }
}
