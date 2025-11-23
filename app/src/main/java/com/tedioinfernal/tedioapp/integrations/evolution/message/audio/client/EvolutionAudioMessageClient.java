package com.tedioinfernal.tedioapp.integrations.evolution.message.audio.client;

import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto.SendAudioRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.message.audio.dto.SendAudioResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionAudioMessageClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Envia mensagem de áudio via Evolution API
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados do áudio (number e audio em base64)
     * @return Response com dados da mensagem enviada
     */
    public SendAudioResponseDTO sendAudio(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendAudioRequestDTO request) {
        
        log.info("Sending audio message to {} via instance: {}", request.getNumber(), instanceName);
        
        try {
            SendAudioResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/message/sendWhatsAppAudio/" + instanceName)
                    .header("Content-Type", "application/json")
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SendAudioResponseDTO.class)
                    .block();
            
            log.info("Audio message sent successfully. Message ID: {}, Status: {}, Duration: {}s", 
                    response.getKey().getId(), 
                    response.getStatus(),
                    response.getMessage().getAudioMessage() != null 
                        ? response.getMessage().getAudioMessage().getSeconds() 
                        : "N/A");
            return response;
            
        } catch (Exception e) {
            log.error("Error sending audio message: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem de áudio: " + e.getMessage(), e);
        }
    }

    /**
     * Envia mensagem de áudio via Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param request Dados do áudio (number e audio em base64)
     * @return Mono com response da mensagem enviada
     */
    public Mono<SendAudioResponseDTO> sendAudioAsync(
            String baseUrl,
            String apiKey,
            String instanceName,
            SendAudioRequestDTO request) {
        
        log.info("Sending audio message (async) to {} via instance: {}", 
                request.getNumber(), instanceName);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/message/sendWhatsAppAudio/" + instanceName)
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SendAudioResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Audio message sent successfully (async). Message ID: {}, Status: {}, Duration: {}s", 
                            response.getKey().getId(),
                            response.getStatus(),
                            response.getMessage().getAudioMessage() != null 
                                ? response.getMessage().getAudioMessage().getSeconds() 
                                : "N/A"))
                .doOnError(error -> 
                    log.error("Error sending audio message (async): {}", error.getMessage()));
    }
}
