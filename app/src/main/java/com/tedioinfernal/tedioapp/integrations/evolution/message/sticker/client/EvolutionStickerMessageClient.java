package com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.client;

import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto.SendStickerRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.sticker.dto.SendStickerResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionStickerMessageClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Envia sticker via Evolution API
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados do sticker (number e sticker em base64)
     * @return Response com dados da mensagem enviada
     */
    public SendStickerResponseDTO sendSticker(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendStickerRequestDTO request) {
        
        log.info("Sending sticker to {} via instance: {}", request.getNumber(), instanceName);
        
        try {
            SendStickerResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/message/sendSticker/" + instanceName)
                    .header("Content-Type", "application/json")
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SendStickerResponseDTO.class)
                    .block();
            
            log.info("Sticker sent successfully. Message ID: {}, Status: {}", 
                    response.getKey().getId(), response.getStatus());
            return response;
            
        } catch (Exception e) {
            log.error("Error sending sticker: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar sticker: " + e.getMessage(), e);
        }
    }

    /**
     * Envia sticker via Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados do sticker (number e sticker em base64)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendStickerResponseDTO> sendStickerAsync(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendStickerRequestDTO request) {
        
        log.info("Sending sticker (async) to {} via instance: {}", 
                request.getNumber(), instanceName);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/message/sendSticker/" + instanceName)
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SendStickerResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Sticker sent successfully (async). Message ID: {}, Status: {}", 
                            response.getKey().getId(), response.getStatus()))
                .doOnError(error -> 
                    log.error("Error sending sticker (async): {}", error.getMessage()));
    }
}
