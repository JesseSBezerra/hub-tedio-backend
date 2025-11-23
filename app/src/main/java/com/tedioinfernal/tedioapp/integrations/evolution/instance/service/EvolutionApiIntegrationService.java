package com.tedioinfernal.tedioapp.integrations.evolution.instance.service;

import com.tedioinfernal.tedioapp.entity.Evolution;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.client.EvolutionInstanceClient;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.CreateInstanceRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.CreateInstanceResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service para integração com Evolution API
 * Recebe o objeto Evolution (com url e apiKey) e o request DTO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionApiIntegrationService {

    private final EvolutionInstanceClient evolutionInstanceClient;

    /**
     * Cria uma nova instância no Evolution API
     * 
     * @param evolution Objeto Evolution contendo url e apiKey
     * @param request Dados da instância a ser criada
     * @return Response com dados da instância criada
     */
    public CreateInstanceResponseDTO createInstance(
            Evolution evolution,
            CreateInstanceRequestDTO request) {
        
        log.info("Creating instance '{}' using Evolution: {}", 
                request.getInstanceName(), evolution.getNome());
        
        validateEvolution(evolution);
        validateRequest(request);
        
        try {
            CreateInstanceResponseDTO response = evolutionInstanceClient.createInstance(
                    evolution.getUrl(),
                    evolution.getApiKey(),
                    request
            );
            
            log.info("Instance created successfully. ID: {}, Status: {}", 
                    response.getInstance().getInstanceId(),
                    response.getInstance().getStatus());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error creating instance '{}': {}", request.getInstanceName(), e.getMessage());
            throw new RuntimeException("Falha ao criar instância: " + e.getMessage(), e);
        }
    }

    /**
     * Cria uma nova instância no Evolution API (versão reativa)
     * 
     * @param evolution Objeto Evolution contendo url e apiKey
     * @param request Dados da instância a ser criada
     * @return Mono com response da instância criada
     */
    public Mono<CreateInstanceResponseDTO> createInstanceAsync(
            Evolution evolution,
            CreateInstanceRequestDTO request) {
        
        log.info("Creating instance '{}' using Evolution: {} (async)", 
                request.getInstanceName(), evolution.getNome());
        
        validateEvolution(evolution);
        validateRequest(request);
        
        return evolutionInstanceClient.createInstanceAsync(
                evolution.getUrl(),
                evolution.getApiKey(),
                request
        )
        .doOnSuccess(response -> 
            log.info("Instance created successfully (async). ID: {}, Status: {}", 
                    response.getInstance().getInstanceId(),
                    response.getInstance().getStatus()))
        .doOnError(error -> 
            log.error("Error creating instance '{}' (async): {}", 
                    request.getInstanceName(), error.getMessage()));
    }

    /**
     * Valida o objeto Evolution
     */
    private void validateEvolution(Evolution evolution) {
        if (evolution == null) {
            throw new IllegalArgumentException("Evolution não pode ser nulo");
        }
        if (evolution.getUrl() == null || evolution.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("URL da Evolution não pode ser vazia");
        }
        if (evolution.getApiKey() == null || evolution.getApiKey().trim().isEmpty()) {
            throw new IllegalArgumentException("API Key da Evolution não pode ser vazia");
        }
    }

    /**
     * Valida o request
     */
    private void validateRequest(CreateInstanceRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }
        if (request.getInstanceName() == null || request.getInstanceName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da instância não pode ser vazio");
        }
        if (request.getIntegration() == null || request.getIntegration().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de integração não pode ser vazio");
        }
    }
}
