package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.EvolutionRequestDTO;
import com.tedioinfernal.tedioapp.dto.EvolutionResponseDTO;
import com.tedioinfernal.tedioapp.entity.Evolution;
import com.tedioinfernal.tedioapp.entity.Owner;
import com.tedioinfernal.tedioapp.repository.EvolutionRepository;
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
public class EvolutionService {

    private final EvolutionRepository evolutionRepository;
    private final OwnerRepository ownerRepository;

    @Transactional
    public EvolutionResponseDTO createEvolution(EvolutionRequestDTO requestDTO) {
        log.info("Creating evolution with name: {}", requestDTO.getNome());

        if (evolutionRepository.existsByNome(requestDTO.getNome())) {
            throw new RuntimeException("Evolution com nome '" + requestDTO.getNome() + "' já existe");
        }

        Owner owner = ownerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner não encontrado com ID: " + requestDTO.getOwnerId()));

        Evolution evolution = Evolution.builder()
                .nome(requestDTO.getNome())
                .descricao(requestDTO.getDescricao())
                .url(requestDTO.getUrl())
                .apiKey(requestDTO.getApiKey())
                .owner(owner)
                .build();

        Evolution savedEvolution = evolutionRepository.save(evolution);
        log.info("Evolution created successfully with ID: {}", savedEvolution.getId());

        return mapToResponseDTO(savedEvolution);
    }

    @Transactional(readOnly = true)
    public EvolutionResponseDTO getEvolutionById(Long id) {
        log.info("Fetching evolution with ID: {}", id);
        Evolution evolution = evolutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evolution não encontrada com ID: " + id));
        return mapToResponseDTO(evolution);
    }

    @Transactional(readOnly = true)
    public EvolutionResponseDTO getEvolutionByNome(String nome) {
        log.info("Fetching evolution with name: {}", nome);
        Evolution evolution = evolutionRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Evolution não encontrada com nome: " + nome));
        return mapToResponseDTO(evolution);
    }

    @Transactional(readOnly = true)
    public List<EvolutionResponseDTO> getAllEvolutions() {
        log.info("Fetching all evolutions");
        return evolutionRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EvolutionResponseDTO> getEvolutionsByOwnerId(Long ownerId) {
        log.info("Fetching evolutions for owner ID: {}", ownerId);
        return evolutionRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EvolutionResponseDTO updateEvolution(Long id, EvolutionRequestDTO requestDTO) {
        log.info("Updating evolution with ID: {}", id);

        Evolution evolution = evolutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evolution não encontrada com ID: " + id));

        // Verifica se o novo nome já existe em outra evolution
        if (!evolution.getNome().equals(requestDTO.getNome()) && 
            evolutionRepository.existsByNome(requestDTO.getNome())) {
            throw new RuntimeException("Evolution com nome '" + requestDTO.getNome() + "' já existe");
        }

        Owner owner = ownerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner não encontrado com ID: " + requestDTO.getOwnerId()));

        evolution.setNome(requestDTO.getNome());
        evolution.setDescricao(requestDTO.getDescricao());
        evolution.setUrl(requestDTO.getUrl());
        evolution.setApiKey(requestDTO.getApiKey());
        evolution.setOwner(owner);

        Evolution updatedEvolution = evolutionRepository.save(evolution);
        log.info("Evolution updated successfully with ID: {}", updatedEvolution.getId());

        return mapToResponseDTO(updatedEvolution);
    }

    @Transactional
    public void deleteEvolution(Long id) {
        log.info("Deleting evolution with ID: {}", id);

        if (!evolutionRepository.existsById(id)) {
            throw new RuntimeException("Evolution não encontrada com ID: " + id);
        }

        evolutionRepository.deleteById(id);
        log.info("Evolution deleted successfully with ID: {}", id);
    }

    private EvolutionResponseDTO mapToResponseDTO(Evolution evolution) {
        return EvolutionResponseDTO.builder()
                .id(evolution.getId())
                .nome(evolution.getNome())
                .descricao(evolution.getDescricao())
                .url(evolution.getUrl())
                .apiKey(evolution.getApiKey())
                .ownerId(evolution.getOwner().getId())
                .ownerNome(evolution.getOwner().getNome())
                .createdAt(evolution.getCreatedAt())
                .updatedAt(evolution.getUpdatedAt())
                .build();
    }
}
