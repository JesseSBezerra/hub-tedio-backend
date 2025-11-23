package com.tedioinfernal.tedioapp.integrations.evolution.message.text.service;

import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.client.EvolutionTextMessageClient;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.SendTextRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.SendTextResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service para envio de mensagens de texto via Evolution API
 * Recebe o objeto EvolutionInstance (com instanceName, evolution.url e evolution.apiKey)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionTextMessageService {

    private final EvolutionTextMessageClient evolutionTextMessageClient;

    /**
     * Envia mensagem de texto via Evolution API
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados da mensagem (number e text)
     * @return Response com dados da mensagem enviada
     */
    public SendTextResponseDTO sendText(
            EvolutionInstance evolutionInstance,
            SendTextRequestDTO request) {
        
        log.info("Sending text message via instance: {} to number: {}", 
                evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        try {
            SendTextResponseDTO response = evolutionTextMessageClient.sendText(
                    evolutionInstance.getEvolution().getUrl(),
                    evolutionInstance.getEvolution().getApiKey(),
                    evolutionInstance.getInstanceName(),
                    request
            );
            
            log.info("Text message sent successfully. Message ID: {}, Status: {}, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getInstanceId());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error sending text message via instance '{}': {}", 
                    evolutionInstance.getInstanceName(), e.getMessage());
            throw new RuntimeException("Falha ao enviar mensagem de texto: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de texto via Evolution API (versão reativa)
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados da mensagem (number e text)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendTextResponseDTO> sendTextAsync(
            EvolutionInstance evolutionInstance,
            SendTextRequestDTO request) {
        
        log.info("Sending text message (async) via instance: {} to number: {}", 
                evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        return evolutionTextMessageClient.sendTextAsync(
                evolutionInstance.getEvolution().getUrl(),
                evolutionInstance.getEvolution().getApiKey(),
                evolutionInstance.getInstanceName(),
                request
        )
        .doOnSuccess(response -> 
            log.info("Text message sent successfully (async). Message ID: {}, Status: {}, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getInstanceId()))
        .doOnError(error -> 
            log.error("Error sending text message (async) via instance '{}': {}", 
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
    private void validateRequest(SendTextRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getNumber() == null || request.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número de telefone não pode ser vazio");
        }
        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Texto da mensagem não pode ser vazio");
        }
    }
}
