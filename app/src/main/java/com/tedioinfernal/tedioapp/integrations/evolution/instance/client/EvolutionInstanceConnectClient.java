package com.tedioinfernal.tedioapp.integrations.evolution.instance.client;

import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.ConnectResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceConnectClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Conecta uma instância e obtém QR Code no Evolution API (versão síncrona)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Response com QR Code e dados de conexão
     */
    public ConnectResponseDTO connect(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Connecting Evolution instance: {} at {}", instanceName, baseUrl);
        
        try {
            ConnectResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri("/instance/connect/{instanceName}", instanceName)
                    .header("apikey", apiKey)
                    .retrieve()
                    .bodyToMono(ConnectResponseDTO.class)
                    .block();
            
            log.info("Instance connected successfully: {} - QR Code generated (count: {})", 
                    instanceName, response.getCount());
            return response;
            
        } catch (Exception e) {
            log.error("Error connecting Evolution instance '{}': {}", instanceName, e.getMessage(), e);
            throw new RuntimeException("Falha ao conectar instância: " + e.getMessage(), e);
        }
    }

    /**
     * Conecta uma instância e obtém QR Code no Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Mono com response de conexão
     */
    public Mono<ConnectResponseDTO> connectAsync(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Connecting Evolution instance (async): {} at {}", instanceName, baseUrl);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri("/instance/connect/{instanceName}", instanceName)
                .header("apikey", apiKey)
                .retrieve()
                .bodyToMono(ConnectResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Instance connected successfully (async): {} - QR Code generated (count: {})", 
                            instanceName, response.getCount()))
                .doOnError(error -> 
                    log.error("Error connecting Evolution instance (async) '{}': {}", 
                            instanceName, error.getMessage()));
    }
}
