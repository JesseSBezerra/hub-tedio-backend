package com.tedioinfernal.tedioapp.integrations.evolution.instance.client;

import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.DeleteInstanceResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceDeleteClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Deleta uma instância no Evolution API (versão síncrona)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Response com status da deleção
     */
    public DeleteInstanceResponseDTO deleteInstance(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Deleting Evolution instance from API: {} at {}", instanceName, baseUrl);
        
        try {
            DeleteInstanceResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .delete()
                    .uri("/instance/delete/{instanceName}", instanceName)
                    .header("apikey", apiKey)
                    .retrieve()
                    .bodyToMono(DeleteInstanceResponseDTO.class)
                    .block();
            
            log.info("Instance deleted from API successfully: {}", instanceName);
            return response;
            
        } catch (Exception e) {
            log.error("Error deleting Evolution instance from API '{}': {}", instanceName, e.getMessage(), e);
            throw new RuntimeException("Falha ao deletar instância da API Evolution: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta uma instância no Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param instanceName Nome da instância
     * @return Mono com response da deleção
     */
    public Mono<DeleteInstanceResponseDTO> deleteInstanceAsync(String baseUrl, String apiKey, String instanceName) {
        
        log.info("Deleting Evolution instance from API (async): {} at {}", instanceName, baseUrl);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .delete()
                .uri("/instance/delete/{instanceName}", instanceName)
                .header("apikey", apiKey)
                .retrieve()
                .bodyToMono(DeleteInstanceResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Instance deleted from API successfully (async): {}", instanceName))
                .doOnError(error -> 
                    log.error("Error deleting Evolution instance from API (async) '{}': {}", 
                            instanceName, error.getMessage()));
    }
}
