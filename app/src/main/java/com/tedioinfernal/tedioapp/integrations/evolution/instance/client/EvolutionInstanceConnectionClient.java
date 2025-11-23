package com.tedioinfernal.tedioapp.integrations.evolution.instance.client;

import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.ConnectionStateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceConnectionClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Verifica o estado da conexão de uma instância no Evolution API (versão síncrona)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Response com estado da conexão
     */
    public ConnectionStateDTO getConnectionState(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Getting connection state for Evolution instance: {} at {}", instanceName, baseUrl);
        
        try {
            ConnectionStateDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .get()
                    .uri("/instance/connectionState/{instanceName}", instanceName)
                    .header("apikey", apiKey)
                    .retrieve()
                    .bodyToMono(ConnectionStateDTO.class)
                    .block();
            
            log.info("Connection state retrieved successfully: {} - State: {}", 
                    response.getInstance().getInstanceName(), response.getState());
            return response;
            
        } catch (Exception e) {
            log.error("Error getting connection state for Evolution instance '{}': {}", instanceName, e.getMessage(), e);
            throw new RuntimeException("Falha ao obter estado da conexão: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica o estado da conexão de uma instância no Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Mono com response do estado da conexão
     */
    public Mono<ConnectionStateDTO> getConnectionStateAsync(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Getting connection state (async) for Evolution instance: {} at {}", instanceName, baseUrl);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri("/instance/connectionState/{instanceName}", instanceName)
                .header("apikey", apiKey)
                .retrieve()
                .bodyToMono(ConnectionStateDTO.class)
                .doOnSuccess(response -> 
                    log.info("Connection state retrieved successfully (async): {} - State: {}", 
                            response.getInstance().getInstanceName(), response.getState()))
                .doOnError(error -> 
                    log.error("Error getting connection state (async) for Evolution instance '{}': {}", 
                            instanceName, error.getMessage()));
    }
}
