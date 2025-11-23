package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.OwnerRequestDTO;
import com.tedioinfernal.tedioapp.dto.OwnerResponseDTO;
import com.tedioinfernal.tedioapp.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Slf4j
public class OwnerController {

    private final OwnerService ownerService;

    @PostMapping
    public ResponseEntity<OwnerResponseDTO> createOwner(@Valid @RequestBody OwnerRequestDTO requestDTO) {
        log.info("POST /api/owner - Creating owner");
        OwnerResponseDTO response = ownerService.createOwner(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> getOwnerById(@PathVariable Long id) {
        log.info("GET /api/owner/{} - Fetching owner", id);
        OwnerResponseDTO response = ownerService.getOwnerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OwnerResponseDTO>> getAllOwners() {
        log.info("GET /api/owner - Fetching all owners");
        List<OwnerResponseDTO> response = ownerService.getAllOwners();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> updateOwner(
            @PathVariable Long id,
            @Valid @RequestBody OwnerRequestDTO requestDTO) {
        log.info("PUT /api/owner/{} - Updating owner", id);
        OwnerResponseDTO response = ownerService.updateOwner(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        log.info("DELETE /api/owner/{} - Deleting owner", id);
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
}
