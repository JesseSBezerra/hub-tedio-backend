package com.tedioinfernal.tedioapp.integrations.evolution.instance.client;

import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.CreateInstanceRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.CreateInstanceResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Cria uma nova instância no Evolution API
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param request Dados da instância a ser criada
     * @return Response com dados da instância criada
     */
    public CreateInstanceResponseDTO createInstance(
            String baseUrl, 
            String apiKey, 
            CreateInstanceRequestDTO request) {
        
        log.info("Creating Evolution instance: {} at {}", request.getInstanceName(), baseUrl);
        
        try {
            CreateInstanceResponseDTO response = webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/instance/create")
                    .header("Content-Type", "application/json")
                    .header("apikey", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CreateInstanceResponseDTO.class)
                    .block();
            
            log.info("Instance created successfully: {}", response.getInstance().getInstanceId());
            return response;
            
        } catch (Exception e) {
            log.error("Error creating Evolution instance: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao criar instância Evolution: " + e.getMessage(), e);
        }
    }

    /**
     * Cria uma nova instância no Evolution API (versão reativa)
     * 
     * @param baseUrl URL base da instância Evolution
     * @param apiKey API Key para autenticação
     * @param request Dados da instância a ser criada
     * @return Mono com response da instância criada
     */
    public Mono<CreateInstanceResponseDTO> createInstanceAsync(
            String baseUrl, 
            String apiKey, 
            CreateInstanceRequestDTO request) {
        
        log.info("Creating Evolution instance (async): {} at {}", request.getInstanceName(), baseUrl);
        
        return webClientBuilder
                .baseUrl(baseUrl)
                .build()
                .post()
                .uri("/instance/create")
                .header("Content-Type", "application/json")
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateInstanceResponseDTO.class)
                .doOnSuccess(response -> 
                    log.info("Instance created successfully (async): {}", 
                        response.getInstance().getInstanceId()))
                .doOnError(error -> 
                    log.error("Error creating Evolution instance (async): {}", error.getMessage()));
    }
}
