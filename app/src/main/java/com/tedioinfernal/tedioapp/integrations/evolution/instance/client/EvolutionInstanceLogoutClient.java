package com.tedioinfernal.tedioapp.integrations.evolution.instance.client;

import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.LogoutResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceLogoutClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Faz logout de uma instância no Evolution API (versão síncrona)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Response com status do logout
     */
    public LogoutResponseDTO logout(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Logging out Evolution instance: {} at {}", instanceName, baseUrl);
        
        try {
            LogoutResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .delete()
                    .uri("/instance/logout/{instanceName}", instanceName)
                    .header("apikey", apiKey)
                    .retrieve()
                    .bodyToMono(LogoutResponseDTO.class)
                    .block();
            
            log.info("Instance logged out successfully: {}", instanceName);
            return response;
            
        } catch (Exception e) {
            log.error("Error logging out Evolution instance '{}': {}", instanceName, e.getMessage(), e);
            throw new RuntimeException("Falha ao fazer logout da instância: " + e.getMessage(), e);
        }
    }

    /**
     * Faz logout de uma instância no Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Mono com response do logout
     */
    public Mono<LogoutResponseDTO> logoutAsync(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Logging out Evolution instance (async): {} at {}", instanceName, baseUrl);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .delete()
                .uri("/instance/logout/{instanceName}", instanceName)
                .header("apikey", apiKey)
                .retrieve()
                .bodyToMono(LogoutResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Instance logged out successfully (async): {}", instanceName))
                .doOnError(error -> 
                    log.error("Error logging out Evolution instance (async) '{}': {}", 
                            instanceName, error.getMessage()));
    }
}
