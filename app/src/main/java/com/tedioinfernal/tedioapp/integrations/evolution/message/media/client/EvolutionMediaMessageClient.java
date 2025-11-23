package com.tedioinfernal.tedioapp.integrations.evolution.message.media.client;

import com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto.SendMediaRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.media.dto.SendMediaResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionMediaMessageClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Envia mensagem de mídia via Evolution API
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados da mídia (number, mediatype, mimetype, media, fileName)
     * @return Response com dados da mensagem enviada
     */
    public SendMediaResponseDTO sendMedia(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendMediaRequestDTO request) {
        
        log.info("Sending media message ({}) to {} via instance: {}", 
                request.getMediatype(), request.getNumber(), instanceName);
        
        try {
            SendMediaResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/message/sendMedia/" + instanceName)
                    .header("Content-Type", "application/json")
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SendMediaResponseDTO.class)
                    .block();
            
            log.info("Media message sent successfully. Message ID: {}, Status: {}, Type: {}", 
                    response.getKey().getId(), response.getStatus(), response.getMessageType());
            return response;
            
        } catch (Exception e) {
            log.error("Error sending media message: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem de mídia: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de mídia via Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados da mídia (number, mediatype, mimetype, media, fileName)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendMediaResponseDTO> sendMediaAsync(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendMediaRequestDTO request) {
        
        log.info("Sending media message (async) ({}) to {} via instance: {}", 
                request.getMediatype(), request.getNumber(), instanceName);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/message/sendMedia/" + instanceName)
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SendMediaResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Media message sent successfully (async). Message ID: {}, Status: {}, Type: {}", 
                            response.getKey().getId(), response.getStatus(), response.getMessageType()))
                .doOnError(error -> 
                    log.error("Error sending media message (async): {}", error.getMessage()));
    }
}
