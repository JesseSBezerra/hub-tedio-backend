package com.tedioinfernal.tedioapp.integrations.evolution.media.client;

import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.GetMediaBase64RequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.media.dto.GetMediaBase64ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionMediaClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Busca base64 de mensagem de mídia via Evolution API
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados da mensagem (message.key.id e convertToMp4)
     * @return Response com dados da mídia em base64
     */
    public GetMediaBase64ResponseDTO getBase64FromMediaMessage(
            String baseUrl,
            String apiKey,
            String instanceName,
            GetMediaBase64RequestDTO request) {
        
        log.info("Getting base64 from media message ID: {} via instance: {}", 
                request.getMessage().getKey().getId(), instanceName);
        
        try {
            GetMediaBase64ResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/chat/getBase64FromMediaMessage/" + instanceName)
                    .header("Content-Type", "application/json")
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GetMediaBase64ResponseDTO.class)
                    .block();
            
            log.info("Media base64 retrieved successfully. Media Type: {}, File: {}, Size: {} bytes", 
                    response.getMediaType(), 
                    response.getFileName(),
                    response.getSize() != null ? response.getSize().getFileLength() : "unknown");
            return response;
            
        } catch (Exception e) {
            log.error("Error getting base64 from media message: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao buscar base64 da mensagem de mídia: " + e.getMessage(), e);
        }
    }

    /**
     * Busca base64 de mensagem de mídia via Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados da mensagem (message.key.id e convertToMp4)
     * @return Mono com response da mídia em base64
     */
    public Mono<GetMediaBase64ResponseDTO> getBase64FromMediaMessageAsync(
            String baseUrl,
            String apiKey,
            String instanceName,
            GetMediaBase64RequestDTO request) {
        
        log.info("Getting base64 from media message (async) ID: {} via instance: {}", 
                request.getMessage().getKey().getId(), instanceName);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/chat/getBase64FromMediaMessage/" + instanceName)
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GetMediaBase64ResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Media base64 retrieved successfully (async). Media Type: {}, File: {}", 
                            response.getMediaType(), response.getFileName()))
                .doOnError(error -> 
                    log.error("Error getting base64 from media message (async): {}", error.getMessage()));
    }
}
