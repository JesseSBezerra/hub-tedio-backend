package com.tedioinfernal.tedioapp.integrations.evolution.message.text.client;

import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.SendTextRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.text.dto.SendTextResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionTextMessageClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Envia mensagem de texto via Evolution API
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados da mensagem (number e text)
     * @return Response com dados da mensagem enviada
     */
    public SendTextResponseDTO sendText(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendTextRequestDTO request) {
        
        log.info("Sending text message to {} via instance: {}", request.getNumber(), instanceName);
        
        try {
            SendTextResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/message/sendText/" + instanceName)
                    .header("Content-Type", "application/json")
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SendTextResponseDTO.class)
                    .block();
            
            log.info("Text message sent successfully. Message ID: {}, Status: {}", 
                    response.getKey().getId(), response.getStatus());
            return response;
            
        } catch (Exception e) {
            log.error("Error sending text message: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem de texto: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de texto via Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados da mensagem (number e text)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendTextResponseDTO> sendTextAsync(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendTextRequestDTO request) {
        
        log.info("Sending text message (async) to {} via instance: {}", 
                request.getNumber(), instanceName);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/message/sendText/" + instanceName)
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SendTextResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Text message sent successfully (async). Message ID: {}, Status: {}", 
                            response.getKey().getId(), response.getStatus()))
                .doOnError(error -> 
                    log.error("Error sending text message (async): {}", error.getMessage()));
    }
}
