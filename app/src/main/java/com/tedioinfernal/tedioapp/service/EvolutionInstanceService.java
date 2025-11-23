package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.EvolutionInstanceRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionInstanceResponseDTO;
import com.tedioinfernal.tedioapp.entity.Evolution;
import com.tedioinfernal.tedioapp.entity.EvolutionInstance;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.CreateInstanceRequestDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.CreateInstanceResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.LogoutResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.ConnectionStateDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.ConnectResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.dto.DeleteInstanceResponseDTO;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.service.EvolutionApiIntegrationService;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.client.EvolutionInstanceLogoutClient;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.client.EvolutionInstanceConnectionClient;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.client.EvolutionInstanceConnectClient;
import com.tedioinfernal.tedioapp.integrations.evolution.instance.client.EvolutionInstanceDeleteClient;
import com.tedioinfernal.tedioapp.integrations.evolution.webhook.client.EvolutionWebhookClient;
import com.tedioinfernal.tedioapp.integrations.evolution.webhook.dto.SetWebhookResponseDTO;
import com.tedioinfernal.tedioapp.dto.SetWebhookInstanceRequestDTO;
import com.tedioinfernal.tedioapp.repository.EvolutionInstanceRepository;
import com.tedioinfernal.tedioapp.repository.EvolutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvolutionInstanceService {

    private final EvolutionInstanceRepository evolutionInstanceRepository;
    private final EvolutionRepository evolutionRepository;
    private final EvolutionApiIntegrationService evolutionApiIntegrationService;
    private final EvolutionInstanceLogoutClient evolutionInstanceLogoutClient;
    private final EvolutionInstanceConnectionClient evolutionInstanceConnectionClient;
    private final EvolutionInstanceConnectClient evolutionInstanceConnectClient;
    private final EvolutionInstanceDeleteClient evolutionInstanceDeleteClient;
    private final EvolutionWebhookClient evolutionWebhookClient;

    /**
     * Cria uma nova instância Evolution
     * 1. Valida dados
     * 2. Busca Evolution configurada
     * 3. Chama API Evolution para criar instância
     * 4. Persiste dados apenas se sucesso
     */
    @Transactional
    public EvolutionInstanceResponseDTO createInstance(
            EvolutionInstanceRequestDTO requestDTO,
            User currentUser) {
        
        log.info("Creating Evolution instance '{}' for user: {}", 
                requestDTO.getInstanceName(), currentUser.getEmail());

        // Valida se nome já existe
        if (evolutionInstanceRepository.existsByInstanceName(requestDTO.getInstanceName())) {
            throw new RuntimeException("Já existe uma instância com o nome: " + requestDTO.getInstanceName());
        }

        // Busca Evolution configurada
        Evolution evolution = evolutionRepository.findById(requestDTO.getEvolutionId())
                .orElseThrow(() -> new RuntimeException("Evolution não encontrada com ID: " + requestDTO.getEvolutionId()));

        log.info("Using Evolution: {} ({})", evolution.getNome(), evolution.getUrl());

        // Prepara request para API Evolution
        CreateInstanceRequestDTO integrationRequest = CreateInstanceRequestDTO.builder()
                .instanceName(requestDTO.getInstanceName())
                .qrcode(requestDTO.getQrcode())
                .integration(requestDTO.getIntegration())
                .build();

        try {
            // Chama API Evolution para criar instância
            log.info("Calling Evolution API to create instance...");
            CreateInstanceResponseDTO integrationResponse = 
                    evolutionApiIntegrationService.createInstance(evolution, integrationRequest);

            log.info("Evolution API responded successfully. Instance ID: {}", 
                    integrationResponse.getInstance().getInstanceId());

            // Cria entidade para persistir
            EvolutionInstance evolutionInstance = EvolutionInstance.builder()
                    .instanceName(requestDTO.getInstanceName())
                    .instanceId(integrationResponse.getInstance().getInstanceId())
                    .qrcode(requestDTO.getQrcode())
                    .qrcodeBase64(requestDTO.getQrcode() && integrationResponse.getQrcode() != null 
                            ? integrationResponse.getQrcode().getBase64() 
                            : null)
                    .integration(requestDTO.getIntegration())
                    .status(integrationResponse.getInstance().getStatus())
                    .hash(integrationResponse.getHash())
                    .evolution(evolution)
                    .user(currentUser)
                    .build();

            // Persiste apenas após sucesso na API Evolution
            EvolutionInstance savedInstance = evolutionInstanceRepository.save(evolutionInstance);
            
            log.info("Evolution instance persisted successfully with ID: {}", savedInstance.getId());

            return mapToResponseDTO(savedInstance);

        } catch (Exception e) {
            log.error("Error creating Evolution instance: {}", e.getMessage(), e);
            throw new RuntimeException("Falha ao criar instância Evolution: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public EvolutionInstanceResponseDTO getInstanceById(Long id) {
        log.info("Fetching Evolution instance with ID: {}", id);
        EvolutionInstance instance = evolutionInstanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com ID: " + id));
        return mapToResponseDTO(instance);
    }

    @Transactional(readOnly = true)
    public EvolutionInstanceResponseDTO getInstanceByName(String instanceName) {
        log.info("Fetching Evolution instance with name: {}", instanceName);
        EvolutionInstance instance = evolutionInstanceRepository.findByInstanceName(instanceName)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com nome: " + instanceName));
        return mapToResponseDTO(instance);
    }

    @Transactional(readOnly = true)
    public List<EvolutionInstanceResponseDTO> getAllInstances() {
        log.info("Fetching all Evolution instances");
        return evolutionInstanceRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvolutionInstanceResponseDTO> getInstancesByEvolutionId(Long evolutionId) {
        log.info("Fetching Evolution instances for evolution ID: {}", evolutionId);
        return evolutionInstanceRepository.findByEvolutionId(evolutionId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvolutionInstanceResponseDTO> getInstancesByUserId(Long userId) {
        log.info("Fetching Evolution instances for user ID: {}", userId);
        return evolutionInstanceRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvolutionInstanceResponseDTO> getInstancesByStatus(String status) {
        log.info("Fetching Evolution instances with status: {}", status);
        return evolutionInstanceRepository.findByStatus(status).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deleta uma instância
     * 1. Deleta da API Evolution
     * 2. Deleta do banco de dados
     */
    @Transactional
    public void deleteInstance(Long id) {
        
        log.info("Deleting Evolution instance with ID: {}", id);

        // Busca instância
        EvolutionInstance instance = evolutionInstanceRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com ID: " + id));

        log.info("Instance found: {} (Evolution: {})", 
                instance.getInstanceName(),
                instance.getEvolution().getNome());

        try {
            // 1. Deleta da API Evolution
            log.info("Deleting instance from Evolution API: {}", instance.getInstanceName());
            DeleteInstanceResponseDTO apiResponse = evolutionInstanceDeleteClient.deleteInstance(
                    instance.getEvolution().getUrl(),
                    instance.getEvolution().getApiKey(),
                    instance.getInstanceName()
            );
            log.info("Instance deleted from API successfully: {} - Status: {}", 
                    instance.getInstanceName(), apiResponse.getStatus());

        } catch (Exception e) {
            log.warn("Error deleting instance from Evolution API '{}': {}. Continuing with database deletion.", 
                    instance.getInstanceName(), e.getMessage());
            // Continua mesmo se falhar na API
        }

        // 2. Deleta do banco de dados
        evolutionInstanceRepository.deleteById(id);
        log.info("Evolution instance deleted successfully from database with ID: {}", id);
    }

    /**
     * Conecta uma instância Evolution e obtém QR Code
     */
    @Transactional(readOnly = true)
    public ConnectResponseDTO connectInstance(Long instanceId) {
        
        log.info("Connecting Evolution instance ID: {}", instanceId);

        // Busca instância
        EvolutionInstance instance = evolutionInstanceRepository
                .findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com ID: " + instanceId));

        log.info("Instance found: {} (Evolution: {})", 
                instance.getInstanceName(),
                instance.getEvolution().getNome());

        try {
            // Chama API Evolution para conectar e obter QR Code
            ConnectResponseDTO response = evolutionInstanceConnectClient.connect(
                    instance.getEvolution().getUrl(),
                    instance.getEvolution().getApiKey(),
                    instance.getInstanceName()
            );

            log.info("Instance connected successfully: {} - QR Code count: {}", 
                    instance.getInstanceName(), response.getCount());

            return response;

        } catch (Exception e) {
            log.error("Error connecting instance '{}': {}", instance.getInstanceName(), e.getMessage(), e);
            throw new RuntimeException("Falha ao conectar instância: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica o estado da conexão de uma instância Evolution
     */
    @Transactional(readOnly = true)
    public ConnectionStateDTO getConnectionState(Long instanceId) {
        
        log.info("Getting connection state for Evolution instance ID: {}", instanceId);

        // Busca instância
        EvolutionInstance instance = evolutionInstanceRepository
                .findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com ID: " + instanceId));

        log.info("Instance found: {} (Evolution: {})", 
                instance.getInstanceName(),
                instance.getEvolution().getNome());

        try {
            // Chama API Evolution para obter estado da conexão
            ConnectionStateDTO response = evolutionInstanceConnectionClient.getConnectionState(
                    instance.getEvolution().getUrl(),
                    instance.getEvolution().getApiKey(),
                    instance.getInstanceName()
            );

            log.info("Connection state retrieved successfully: {} - State: {}", 
                    response.getInstance().getInstanceName(), response.getState());

            return response;

        } catch (Exception e) {
            log.error("Error getting connection state for instance '{}': {}", instance.getInstanceName(), e.getMessage(), e);
            throw new RuntimeException("Falha ao obter estado da conexão: " + e.getMessage(), e);
        }
    }

    /**
     * Configura webhook para uma instância Evolution
     * 1. Chama API Evolution para configurar webhook
     * 2. Atualiza URL do webhook no banco de dados
     */
    @Transactional
    public EvolutionInstanceResponseDTO setWebhook(Long instanceId, SetWebhookInstanceRequestDTO requestDTO) {
        
        log.info("Setting webhook for Evolution instance ID: {} - URL: {}", instanceId, requestDTO.getWebhookUrl());

        // Busca instância
        EvolutionInstance instance = evolutionInstanceRepository
                .findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com ID: " + instanceId));

        log.info("Instance found: {} (Evolution: {})", 
                instance.getInstanceName(),
                instance.getEvolution().getNome());

        try {
            // Chama API Evolution para configurar webhook
            evolutionWebhookClient.setWebhook(
                    instance.getEvolution().getUrl(),
                    instance.getEvolution().getApiKey(),
                    instance.getInstanceName(),
                    requestDTO.getWebhookUrl()
            );

            log.info("Webhook configured successfully in API: {}", instance.getInstanceName());

            // Atualiza webhook URL no banco de dados
            instance.setWebhookUrl(requestDTO.getWebhookUrl());
            EvolutionInstance updatedInstance = evolutionInstanceRepository.save(instance);

            log.info("Webhook URL saved in database for instance: {}", instance.getInstanceName());

            return mapToResponseDTO(updatedInstance);

        } catch (Exception e) {
            log.error("Error setting webhook for instance '{}': {}", instance.getInstanceName(), e.getMessage(), e);
            throw new RuntimeException("Falha ao configurar webhook: " + e.getMessage(), e);
        }
    }

    /**
     * Faz logout de uma instância Evolution
     * Chama API Evolution para desconectar a instância
     * NÃO deleta a instância do banco de dados
     */
    @Transactional(readOnly = true)
    public LogoutResponseDTO logoutInstance(Long instanceId) {
        
        log.info("Logging out Evolution instance ID: {}", instanceId);

        // Busca instância
        EvolutionInstance instance = evolutionInstanceRepository
                .findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Instância não encontrada com ID: " + instanceId));

        log.info("Instance found: {} (Evolution: {})", 
                instance.getInstanceName(),
                instance.getEvolution().getNome());

        try {
            // Chama API Evolution para fazer logout
            LogoutResponseDTO response = evolutionInstanceLogoutClient.logout(
                    instance.getEvolution().getUrl(),
                    instance.getEvolution().getApiKey(),
                    instance.getInstanceName()
            );

            log.info("Instance logged out successfully: {} - Status: {}", 
                    instance.getInstanceName(), response.getStatus());

            return response;

        } catch (Exception e) {
            log.error("Error logging out instance '{}': {}", instance.getInstanceName(), e.getMessage(), e);
            throw new RuntimeException("Falha ao fazer logout da instância: " + e.getMessage(), e);
        }
    }

    private EvolutionInstanceResponseDTO mapToResponseDTO(EvolutionInstance instance) {
        return EvolutionInstanceResponseDTO.builder()
                .id(instance.getId())
                .instanceName(instance.getInstanceName())
                .instanceId(instance.getInstanceId())
                .qrcode(instance.getQrcode())
                .qrcodeBase64(instance.getQrcode() ? instance.getQrcodeBase64() : null)
                .integration(instance.getIntegration())
                .status(instance.getStatus())
                .hash(instance.getHash())
                .webhookUrl(instance.getWebhookUrl())
                .evolutionId(instance.getEvolution().getId())
                .evolutionNome(instance.getEvolution().getNome())
                .userId(instance.getUser().getId())
                .userName(instance.getUser().getNome())
                .createdAt(instance.getCreatedAt())
                .updatedAt(instance.getUpdatedAt())
                .build();
    }
}
