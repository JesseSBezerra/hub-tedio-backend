package com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.service;

import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.client.EvolutionStickerMessageClient;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto.SendStickerRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto.SendStickerResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service para envio de stickers via Evolution API
 * Recebe o objeto EvolutionInstance (com instanceName, evolution.url e evolution.apiKey)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionStickerMessageService {

    private final EvolutionStickerMessageClient evolutionStickerMessageClient;

    /**
     * Envia sticker via Evolution API
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados do sticker (number e sticker em base64)
     * @return Response com dados da mensagem enviada
     */
    public SendStickerResponseDTO sendSticker(
            EvolutionInstance evolutionInstance,
            SendStickerRequestDTO request) {
        
        log.info("Sending sticker via instance: {} to number: {}", 
                evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        try {
            SendStickerResponseDTO response = evolutionStickerMessageClient.sendSticker(
                    evolutionInstance.getEvolution().getUrl(),
                    evolutionInstance.getEvolution().getApiKey(),
                    evolutionInstance.getInstanceName(),
                    request
            );
            
            log.info("Sticker sent successfully. Message ID: {}, Status: {}, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getInstanceId());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error sending sticker via instance '{}': {}", 
                    evolutionInstance.getInstanceName(), e.getMessage());
            throw new RuntimeException("Falha ao enviar sticker: " + e.getMessage(), e);
        }
    }

    /**
     * Envia sticker via Evolution API (versão reativa)
     * 
     * @param evolutionInstance Objeto EvolutionInstance contendo instanceName e Evolution (url e apiKey)
     * @param request Dados do sticker (number e sticker em base64)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendStickerResponseDTO> sendStickerAsync(
            EvolutionInstance evolutionInstance,
            SendStickerRequestDTO request) {
        
        log.info("Sending sticker (async) via instance: {} to number: {}", 
                evolutionInstance.getInstanceName(), request.getNumber());
        
        validateEvolutionInstance(evolutionInstance);
        validateRequest(request);
        
        return evolutionStickerMessageClient.sendStickerAsync(
                evolutionInstance.getEvolution().getUrl(),
                evolutionInstance.getEvolution().getApiKey(),
                evolutionInstance.getInstanceName(),
                request
        )
        .doOnSuccess(response -> 
            log.info("Sticker sent successfully (async). Message ID: {}, Status: {}, Instance: {}", 
                    response.getKey().getId(),
                    response.getStatus(),
                    response.getInstanceId()))
        .doOnError(error -> 
            log.error("Error sending sticker (async) via instance '{}': {}", 
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
    private void validateRequest(SendStickerRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getNumber() == null || request.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Número de telefone não pode ser vazio");
        }
        if (request.getSticker() == null || request.getSticker().trim().isEmpty()) {
            throw new IllegalArgumentException("Conteúdo do sticker (base64) não pode ser vazio");
        }
    }
}
