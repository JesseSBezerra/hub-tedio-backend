package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.PathRequestDTO;
import com.tedioinfernal.tedioapp.dto.PathResponseDTO;
import com.tedioinfernal.tedioapp.entity.Integration;
import com.tedioinfernal.tedioapp.entity.Path;
import com.tedioinfernal.tedioapp.repository.IntegrationRepository;
import com.tedioinfernal.tedioapp.repository.PathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PathService {

    private final PathRepository pathRepository;
    private final IntegrationRepository integrationRepository;

    @Transactional
    public PathResponseDTO createPath(PathRequestDTO requestDTO) {
        log.info("Creating path with name: {}", requestDTO.getNome());

        Integration integration = integrationRepository.findById(requestDTO.getIntegrationId())
                .orElseThrow(() -> new RuntimeException("Integration não encontrada"));

        Path path = Path.builder()
                .nome(requestDTO.getNome())
                .path(requestDTO.getPath())
                .integration(integration)
                .build();

        Path savedPath = pathRepository.save(path);
        log.info("Path created successfully with ID: {}", savedPath.getId());

        return mapToResponseDTO(savedPath);
    }

    @Transactional(readOnly = true)
    public PathResponseDTO getPathById(Long id) {
        log.info("Fetching path with ID: {}", id);
        Path path = pathRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Path não encontrado"));
        return mapToResponseDTO(path);
    }

    @Transactional(readOnly = true)
    public List<PathResponseDTO> getAllPaths() {
        log.info("Fetching all paths");
        return pathRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PathResponseDTO> getPathsByIntegrationId(Long integrationId) {
        log.info("Fetching paths for integration ID: {}", integrationId);
        return pathRepository.findByIntegrationId(integrationId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PathResponseDTO updatePath(Long id, PathRequestDTO requestDTO) {
        log.info("Updating path with ID: {}", id);

        Path path = pathRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Path não encontrado"));

        Integration integration = integrationRepository.findById(requestDTO.getIntegrationId())
                .orElseThrow(() -> new RuntimeException("Integration não encontrada"));

        path.setNome(requestDTO.getNome());
        path.setPath(requestDTO.getPath());
        path.setIntegration(integration);

        Path updatedPath = pathRepository.save(path);
        log.info("Path updated successfully with ID: {}", updatedPath.getId());

        return mapToResponseDTO(updatedPath);
    }

    @Transactional
    public void deletePath(Long id) {
        log.info("Deleting path with ID: {}", id);

        if (!pathRepository.existsById(id)) {
            throw new RuntimeException("Path não encontrado");
        }

        pathRepository.deleteById(id);
        log.info("Path deleted successfully with ID: {}", id);
    }

    private PathResponseDTO mapToResponseDTO(Path path) {
        return PathResponseDTO.builder()
                .id(path.getId())
                .nome(path.getNome())
                .path(path.getPath())
                .integrationId(path.getIntegration().getId())
                .integrationNome(path.getIntegration().getNome())
                .createdAt(path.getCreatedAt())
                .updatedAt(path.getUpdatedAt())
                .build();
    }
}
