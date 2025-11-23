package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.IntegrationRequestDTO;
import com.tedioinfernal.tedioapp.dto.IntegrationResponseDTO;
import com.tedioinfernal.tedioapp.entity.ApiAuthentication;
import com.tedioinfernal.tedioapp.entity.Integration;
import com.tedioinfernal.tedioapp.entity.Owner;
import com.tedioinfernal.tedioapp.repository.ApiAuthenticationRepository;
import com.tedioinfernal.tedioapp.repository.IntegrationRepository;
import com.tedioinfernal.tedioapp.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntegrationService {

    private final IntegrationRepository integrationRepository;
    private final OwnerRepository ownerRepository;
    private final ApiAuthenticationRepository apiAuthenticationRepository;

    @Transactional
    public IntegrationResponseDTO createIntegration(IntegrationRequestDTO requestDTO) {
        log.info("Creating integration with name: {}", requestDTO.getNome());

        Owner owner = ownerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner não encontrado"));

        ApiAuthentication authentication = null;
        if (requestDTO.getAuthenticationId() != null) {
            authentication = apiAuthenticationRepository.findById(requestDTO.getAuthenticationId())
                    .orElseThrow(() -> new RuntimeException("Authentication não encontrada"));
        }

        Integration integration = Integration.builder()
                .nome(requestDTO.getNome())
                .description(requestDTO.getDescription())
                .baseUrl(requestDTO.getBaseUrl())
                .owner(owner)
                .authentication(authentication)
                .headers(requestDTO.getHeaders())
                .build();

        Integration savedIntegration = integrationRepository.save(integration);
        log.info("Integration created successfully with ID: {}", savedIntegration.getId());

        return mapToResponseDTO(savedIntegration);
    }

    @Transactional(readOnly = true)
    public IntegrationResponseDTO getIntegrationById(Long id) {
        log.info("Fetching integration with ID: {}", id);
        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration não encontrada"));
        return mapToResponseDTO(integration);
    }

    @Transactional(readOnly = true)
    public List<IntegrationResponseDTO> getAllIntegrations() {
        log.info("Fetching all integrations");
        return integrationRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IntegrationResponseDTO> getIntegrationsByOwnerId(Long ownerId) {
        log.info("Fetching integrations for owner ID: {}", ownerId);
        return integrationRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public IntegrationResponseDTO updateIntegration(Long id, IntegrationRequestDTO requestDTO) {
        log.info("Updating integration with ID: {}", id);

        Integration integration = integrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Integration não encontrada"));

        Owner owner = ownerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner não encontrado"));

        ApiAuthentication authentication = null;
        if (requestDTO.getAuthenticationId() != null) {
            authentication = apiAuthenticationRepository.findById(requestDTO.getAuthenticationId())
                    .orElseThrow(() -> new RuntimeException("Authentication não encontrada"));
        }

        integration.setNome(requestDTO.getNome());
        integration.setDescription(requestDTO.getDescription());
        integration.setBaseUrl(requestDTO.getBaseUrl());
        integration.setOwner(owner);
        integration.setAuthentication(authentication);
        integration.setHeaders(requestDTO.getHeaders());

        Integration updatedIntegration = integrationRepository.save(integration);
        log.info("Integration updated successfully with ID: {}", updatedIntegration.getId());

        return mapToResponseDTO(updatedIntegration);
    }

    @Transactional
    public void deleteIntegration(Long id) {
        log.info("Deleting integration with ID: {}", id);

        if (!integrationRepository.existsById(id)) {
            throw new RuntimeException("Integration não encontrada");
        }

        integrationRepository.deleteById(id);
        log.info("Integration deleted successfully with ID: {}", id);
    }

    private IntegrationResponseDTO mapToResponseDTO(Integration integration) {
        return IntegrationResponseDTO.builder()
                .id(integration.getId())
                .nome(integration.getNome())
                .description(integration.getDescription())
                .baseUrl(integration.getBaseUrl())
                .ownerId(integration.getOwner().getId())
                .ownerNome(integration.getOwner().getNome())
                .authenticationId(integration.getAuthentication() != null ? integration.getAuthentication().getId() : null)
                .authenticationNome(integration.getAuthentication() != null ? integration.getAuthentication().getNome() : null)
                .headers(integration.getHeaders())
                .createdAt(integration.getCreatedAt())
                .updatedAt(integration.getUpdatedAt())
                .build();
    }
}
