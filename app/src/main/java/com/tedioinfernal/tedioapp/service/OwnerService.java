package com.tedioinfernal.tedioapp.service;

import com.tedioinfernal.tedioapp.dto.OwnerRequestDTO;
import com.tedioinfernal.tedioapp.dto.OwnerResponseDTO;
import com.tedioinfernal.tedioapp.entity.Owner;
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
public class OwnerService {

    private final OwnerRepository ownerRepository;

    @Transactional
    public OwnerResponseDTO createOwner(OwnerRequestDTO requestDTO) {
        log.info("Creating owner with name: {}", requestDTO.getNome());

        Owner owner = Owner.builder()
                .nome(requestDTO.getNome())
                .descricao(requestDTO.getDescricao())
                .build();

        Owner savedOwner = ownerRepository.save(owner);
        log.info("Owner created successfully with ID: {}", savedOwner.getId());

        return mapToResponseDTO(savedOwner);
    }

    @Transactional(readOnly = true)
    public OwnerResponseDTO getOwnerById(Long id) {
        log.info("Fetching owner with ID: {}", id);
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner não encontrado"));
        return mapToResponseDTO(owner);
    }

    @Transactional(readOnly = true)
    public List<OwnerResponseDTO> getAllOwners() {
        log.info("Fetching all owners");
        return ownerRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OwnerResponseDTO updateOwner(Long id, OwnerRequestDTO requestDTO) {
        log.info("Updating owner with ID: {}", id);

        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner não encontrado"));

        owner.setNome(requestDTO.getNome());
        owner.setDescricao(requestDTO.getDescricao());

        Owner updatedOwner = ownerRepository.save(owner);
        log.info("Owner updated successfully with ID: {}", updatedOwner.getId());

        return mapToResponseDTO(updatedOwner);
    }

    @Transactional
    public void deleteOwner(Long id) {
        log.info("Deleting owner with ID: {}", id);

        if (!ownerRepository.existsById(id)) {
            throw new RuntimeException("Owner não encontrado");
        }

        ownerRepository.deleteById(id);
        log.info("Owner deleted successfully with ID: {}", id);
    }

    private OwnerResponseDTO mapToResponseDTO(Owner owner) {
        return OwnerResponseDTO.builder()
                .id(owner.getId())
                .nome(owner.getNome())
                .descricao(owner.getDescricao())
                .createdAt(owner.getCreatedAt())
                .updatedAt(owner.getUpdatedAt())
                .build();
    }
}
