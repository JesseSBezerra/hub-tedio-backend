package com.tedioinfernal.tedioapp.integrations.evolution.message.media.service;

import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.client.EvolutionMediaMessageClient;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto.SendMediaRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto.SendMediaResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service para envio de mensagens de mídia via Evolution API
 * Recebe o objeto EvolutionInstance (com instanceName, evolution.url e evolution.apiKey)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionMediaMessageService {

    private final EvolutionMediaMessageClient evolutionMediaMessageClient;

    /**
     * Envia mensagem de mídia via Evolution API
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados da mídia (number, mediatype, mimetype, media, fileName)
     * @return Response com dados da mensagem enviada
     */
    public SendMediaResponseDTO sendMedia(
            EvolutionInstance evolutionInstance,
            SendMediaRequestDTO request) {
        
        log.info("Sending media message ({}) via instance: {} to number: {}", 
                request.getMediatype(), evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        try {
            SendMediaResponseDTO response = evolutionMediaMessageClient.sendMedia(
                    evolutionInstance.getEvolution().getUrl(),
                    evolutionInstance.getEvolution().getApiKey(),
                    evolutionInstance.getInstanceName(),
                    request
            );
            
            log.info("Media message sent successfully. Message ID: {}, Status: {}, Type: {}, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getMessageType(),
                    response.getInstanceId());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error sending media message via instance '{}': {}", 
                    evolutionInstance.getInstanceName(), e.getMessage());
            throw new RuntimeException("Falha ao enviar mensagem de mídia: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de mídia via Evolution API (versão reativa)
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados da mídia (number, mediatype, mimetype, media, fileName)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendMediaResponseDTO> sendMediaAsync(
            EvolutionInstance evolutionInstance,
            SendMediaRequestDTO request) {
        
        log.info("Sending media message (async) ({}) via instance: {} to number: {}", 
                request.getMediatype(), evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        return evolutionMediaMessageClient.sendMediaAsync(
                evolutionInstance.getEvolution().getUrl(),
                evolutionInstance.getEvolution().getApiKey(),
                evolutionInstance.getInstanceName(),
                request
        )
        .doOnSuccess(response -> 
            log.info("Media message sent successfully (async). Message ID: {}, Status: {}, Type: {}, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getMessageType(),
                    response.getInstanceId()))
        .doOnError(error -> 
            log.error("Error sending media message (async) via instance '{}': {}", 
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
    private void validateRequest(SendMediaRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getNumber() == null || request.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número de telefone não pode ser vazio");
        }
        if (request.getMediatype() == null || request.getMediatype().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de mídia não pode ser vazio");
        }
        if (request.getMimetype() == null || request.getMimetype().trim().isEmpty()) {
            throw new IllegalArgumentException("MIME type não pode ser vazio");
        }
        if (request.getMedia() == null || request.getMedia().trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo da mídia (base64) não pode ser vazio");
        }
        if (request.getFileName() == null || request.getFileName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do arquivo não pode ser vazio");
        }
    }
}
