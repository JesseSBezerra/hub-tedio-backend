package com.tedioinfernal.tedioapp.integrations.evolution.media.service;

import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.media.client.EvolutionMediaClient;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.GetMediaBase64RequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.GetMediaBase64ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service para buscar base64 de mensagens de mídia via Evolution API
 * Recebe o objeto EvolutionInstance (com instanceName, evolution.url e evolution.apiKey)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionMediaIntegrationService {

    private final EvolutionMediaClient evolutionMediaClient;

    /**
     * Busca base64 de mensagem de mídia via Evolution API
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados da mensagem (message.key.id e convertToMp4)
     * @return Response com dados da mídia em base64
     */
    public GetMediaBase64ResponseDTO getBase64FromMediaMessage(
            EvolutionInstance evolutionInstance,
            GetMediaBase64RequestDTO request) {
        
        log.info("Getting base64 from media message via instance: {} for message ID: {}", 
                evolutionInstance.getInstanceName(), 
                request.getMessage().getKey().getId());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        try {
            GetMediaBase64ResponseDTO response = evolutionMediaClient.getBase64FromMediaMessage(
                    evolutionInstance.getEvolution().getUrl(),
                    evolutionInstance.getEvolution().getApiKey(),
                    evolutionInstance.getInstanceName(),
                    request
            );
            
            log.info("Media base64 retrieved successfully. Media Type: {}, File: {}, Size: {} bytes, Instance: {}", 
                    response.getMediaType(),
                    response.getFileName(),
                    response.getSize() != null ? response.getSize().getFileLength() : "unknown",
                    evolutionInstance.getInstanceName());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error getting base64 from media message via instance '{}': {}", 
                    evolutionInstance.getInstanceName(), e.getMessage());
            throw new RuntimeException("Falha ao buscar base64 da mensagem de mídia: " + e.getMessage(), e);
        }
    }

    /**
     * Busca base64 de mensagem de mídia via Evolution API (versão reativa)
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados da mensagem (message.key.id e convertToMp4)
     * @return Mono com response da mídia em base64
     */
    public Mono<GetMediaBase64ResponseDTO> getBase64FromMediaMessageAsync(
            EvolutionInstance evolutionInstance,
            GetMediaBase64RequestDTO request) {
        
        log.info("Getting base64 from media message (async) via instance: {} for message ID: {}", 
                evolutionInstance.getInstanceName(),
                request.getMessage().getKey().getId());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        return evolutionMediaClient.getBase64FromMediaMessageAsync(
                evolutionInstance.getEvolution().getUrl(),
                evolutionInstance.getEvolution().getApiKey(),
                evolutionInstance.getInstanceName(),
                request
        )
        .doOnSuccess(response -> 
            log.info("Media base64 retrieved successfully (async). Media Type: {}, File: {}, Instance: {}", 
                    response.getMediaType(),
                    response.getFileName(),
                    evolutionInstance.getInstanceName()))
        .doOnError(error -> 
            log.error("Error getting base64 from media message (async) via instance '{}': {}", 
                    evolutionInstance.getInstanceName(), error.getMessage()));
    }

    /**
     * Valida o objeto EvolutionInstance
     */
    private void validateEvolutionInstance(EvolutionInstance evolutionInstance) {
        if (evolutionInstance == null) {
            throw new IllegalArgumentException("EvolutionInstance não pode ser nulo");
        }
        if (evolutionInstance.getInstanceName() == null || evolutionInstance.getInstanceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da instância não pode ser vazio");
        }
        if (evolutionInstance.getEvolution() == null) {
            throw new IllegalArgumentException("Evolution não pode ser nulo");
        }
        if (evolutionInstance.getEvolution().getUrl() == null || evolutionInstance.getEvolution().getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("URL da Evolution não pode ser vazia");
        }
        if (evolutionInstance.getEvolution().getApiKey() == null || evolutionInstance.getEvolution().getApiKey().trim().isEmpty()) {
            throw new IllegalArgumentException("API Key da Evolution não pode ser vazia");
        }
    }

    /**
     * Valida o request
     */
    private void validateRequest(GetMediaBase64RequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getMessage() == null) {
            throw new IllegalArgumentException("Message não pode ser nulo");
        }
        if (request.getMessage().getKey() == null) {
            throw new IllegalArgumentException("Message key não pode ser nulo");
        }
        if (request.getMessage().getKey().getId() == null || request.getMessage().getKey().getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Message ID não pode ser vazio");
        }
        if (request.getConvertToMp4() == null) {
            throw new IllegalArgumentException("ConvertToMp4 não pode ser nulo");
        }
    }
}
