package com.tedioinfernal.tedioapp.integrations.evolution.webhook.client;

import com.tedioinfernal.tedioapp.integrations.evolution.webhook.dto.SetWebhookRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.webhook.dto.SetWebhookResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.webhook.dto.WebhookConfigDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionWebhookClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Configura webhook para uma instância no Evolution API (versão síncrona)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param webhookUrl URL do webhook
     * @return Response com configuração do webhook
     */
    public SetWebhookResponseDTO setWebhook(String baseUrl, String apiKey, String instanceName, String webhookUrl) {
        
        log.info("Setting webhook for Evolution instance: {} at {} -> Webhook: {}", instanceName, baseUrl, webhookUrl);
        
        try {
            // Prepara headers do webhook
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            // Prepara configuração do webhook com todos os eventos
            WebhookConfigDTO webhookConfig = WebhookConfigDTO.builder()
                    .enabled(true)
                    .url(webhookUrl)
                    .headers(headers)
                    .byEvents(false)
                    .base64(false)
                    .events(Arrays.asList(
                            "APPLICATION_STARTUP",
                            "QRCODE_UPDATED",
                            "MESSAGES_SET",
                            "MESSAGES_UPSERT",
                            "MESSAGES_UPDATE",
                            "MESSAGES_DELETE",
                            "SEND_MESSAGE",
                            "CONTACTS_SET",
                            "CONTACTS_UPSERT",
                            "CONTACTS_UPDATE",
                            "PRESENCE_UPDATE",
                            "CHATS_SET",
                            "CHATS_UPSERT",
                            "CHATS_UPDATE",
                            "CHATS_DELETE",
                            "GROUPS_UPSERT",
                            "GROUP_UPDATE",
                            "GROUP_PARTICIPANTS_UPDATE",
                            "CONNECTION_UPDATE",
                            "LABELS_EDIT",
                            "LABELS_ASSOCIATION",
                            "CALL",
                            "TYPEBOT_START",
                            "TYPEBOT_CHANGE_STATUS"
                    ))
                    .build();
            
            SetWebhookRequestDTO request = SetWebhookRequestDTO.builder()
                    .webhook(webhookConfig)
                    .build();
            
            // Chama API e ignora response (API pode retornar formato variável)
            webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/webhook/set/{instanceName}", instanceName)
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.info("Webhook set successfully for instance: {} - URL: {}", instanceName, webhookUrl);
            
            // Retorna response com a configuração enviada
            return SetWebhookResponseDTO.builder()
                    .webhook(webhookConfig)
                    .build();
            
        } catch (Exception e) {
            log.error("Error setting webhook for Evolution instance '{}': {}", instanceName, e.getMessage(), e);
            throw new RuntimeException("Falha ao configurar webhook: " + e.getMessage(), e);
        }
    }

    /**
     * Configura webhook para uma instância no Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @param webhookUrl URL do webhook
     * @return Mono com response da configuração
     */
    public Mono<SetWebhookResponseDTO> setWebhookAsync(String baseUrl, String apiKey, String instanceName, String webhookUrl) {
        
        log.info("Setting webhook (async) for Evolution instance: {} at {} -> Webhook: {}", instanceName, baseUrl, webhookUrl);
        
        // Prepara headers do webhook
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        // Prepara configuração do webhook com todos os eventos
        WebhookConfigDTO webhookConfig = WebhookConfigDTO.builder()
                .enabled(true)
                .url(webhookUrl)
                .headers(headers)
                .byEvents(false)
                .base64(false)
                .events(Arrays.asList(
                        "APPLICATION_STARTUP",
                        "QRCODE_UPDATED",
                        "MESSAGES_SET",
                        "MESSAGES_UPSERT",
                        "MESSAGES_UPDATE",
                        "MESSAGES_DELETE",
                        "SEND_MESSAGE",
                        "CONTACTS_SET",
                        "CONTACTS_UPSERT",
                        "CONTACTS_UPDATE",
                        "PRESENCE_UPDATE",
                        "CHATS_SET",
                        "CHATS_UPSERT",
                        "CHATS_UPDATE",
                        "CHATS_DELETE",
                        "GROUPS_UPSERT",
                        "GROUP_UPDATE",
                        "GROUP_PARTICIPANTS_UPDATE",
                        "CONNECTION_UPDATE",
                        "LABELS_EDIT",
                        "LABELS_ASSOCIATION",
                        "CALL",
                        "TYPEBOT_START",
                        "TYPEBOT_CHANGE_STATUS"
                ))
                .build();
        
        SetWebhookRequestDTO request = SetWebhookRequestDTO.builder()
                .webhook(webhookConfig)
                .build();
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/webhook/set/{instanceName}", instanceName)
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> SetWebhookResponseDTO.builder()
                        .webhook(webhookConfig)
                        .build())
                .doOnSuccess(response -> 
                    log.info("Webhook set successfully (async) for instance: {} - URL: {}", instanceName, webhookUrl))
                .doOnError(error -> 
                    log.error("Error setting webhook (async) for Evolution instance '{}': {}", 
                            instanceName, error.getMessage()));
    }
}
