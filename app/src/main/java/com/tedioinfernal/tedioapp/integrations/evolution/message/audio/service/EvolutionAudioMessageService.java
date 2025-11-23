package com.tedioinfernal.tedioapp.integrations.evolution.message.audio.service;

import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.client.EvolutionAudioMessageClient;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto.SendAudioRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto.SendAudioResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service para envio de mensagens de áudio via Evolution API
 * Recebe o objeto EvolutionInstance (com instanceName, evolution.url e evolution.apiKey)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionAudioMessageService {

    private final EvolutionAudioMessageClient evolutionAudioMessageClient;

    /**
     * Envia mensagem de áudio via Evolution API
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados do áudio (number e audio em base64)
     * @return Response com dados da mensagem enviada
     */
    public SendAudioResponseDTO sendAudio(
            EvolutionInstance evolutionInstance,
            SendAudioRequestDTO request) {
        
        log.info("Sending audio message via instance: {} to number: {}", 
                evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        try {
            SendAudioResponseDTO response = evolutionAudioMessageClient.sendAudio(
                    evolutionInstance.getEvolution().getUrl(),
                    evolutionInstance.getEvolution().getApiKey(),
                    evolutionInstance.getInstanceName(),
                    request
            );
            
            log.info("Audio message sent successfully. Message ID: {}, Status: {}, Duration: {}s, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getMessage().getAudioMessage() != null 
                        ? response.getMessage().getAudioMessage().getSeconds() 
                        : "N/A",
                    response.getInstanceId());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error sending audio message via instance '{}': {}", 
                    evolutionInstance.getInstanceName(), e.getMessage());
            throw new RuntimeException("Falha ao enviar mensagem de áudio: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de áudio via Evolution API (versão reativa)
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados do áudio (number e audio em base64)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendAudioResponseDTO> sendAudioAsync(
            EvolutionInstance evolutionInstance,
            SendAudioRequestDTO request) {
        
        log.info("Sending audio message (async) via instance: {} to number: {}", 
                evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        return evolutionAudioMessageClient.sendAudioAsync(
                evolutionInstance.getEvolution().getUrl(),
                evolutionInstance.getEvolution().getApiKey(),
                evolutionInstance.getInstanceName(),
                request
        )
        .doOnSuccess(response -> 
            log.info("Audio message sent successfully (async). Message ID: {}, Status: {}, Duration: {}s, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getMessage().getAudioMessage() != null 
                        ? response.getMessage().getAudioMessage().getSeconds() 
                        : "N/A",
                    response.getInstanceId()))
        .doOnError(error -> 
            log.error("Error sending audio message (async) via instance '{}': {}", 
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
    private void validateRequest(SendAudioRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getNumber() == null || request.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número de telefone não pode ser vazio");
        }
        if (request.getAudio() == null || request.getAudio().trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo do áudio (base64) não pode ser vazio");
        }
    }
}
