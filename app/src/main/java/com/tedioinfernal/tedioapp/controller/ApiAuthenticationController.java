package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.ApiAuthenticationRequestDTO;
import com.tedioinfernal.tedioapp.dto.ApiAuthenticationResponseDTO;
import com.tedioinfernal.tedioapp.dto.ApiAuthenticationTestResponseDTO;
import com.tedioinfernal.tedioapp.dto.ResponseFieldsRequestDTO;
import com.tedioinfernal.tedioapp.service.ApiAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/register")
@RequiredArgsConstructor
@Slf4j
public class ApiAuthenticationController {

    private final ApiAuthenticationService apiAuthenticationService;

    @PostMapping("/authentication")
    public ResponseEntity<ApiAuthenticationResponseDTO> createApiAuthentication(
            @Valid @RequestBody ApiAuthenticationRequestDTO requestDTO) {
        log.info("POST /api/register/authentication - Creating API authentication");
        ApiAuthenticationResponseDTO response = apiAuthenticationService.createApiAuthentication(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/authentication/{id}")
    public ResponseEntity<ApiAuthenticationResponseDTO> getApiAuthenticationById(@PathVariable Long id) {
        log.info("GET /api/register/authentication/{} - Fetching API authentication", id);
        ApiAuthenticationResponseDTO response = apiAuthenticationService.getApiAuthenticationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/authentication")
    public ResponseEntity<List<ApiAuthenticationResponseDTO>> getAllApiAuthentications(
            @RequestParam(required = false) Long ownerId) {
        
        if (ownerId != null) {
            log.info("GET /api/register/authentication?ownerId={} - Fetching API authentications by owner", ownerId);
            List<ApiAuthenticationResponseDTO> response = apiAuthenticationService.getApiAuthenticationsByOwnerId(ownerId);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/register/authentication - Fetching all API authentications");
        List<ApiAuthenticationResponseDTO> response = apiAuthenticationService.getAllApiAuthentications();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/authentication/{id}")
    public ResponseEntity<ApiAuthenticationResponseDTO> updateApiAuthentication(
            @PathVariable Long id,
            @Valid @RequestBody ApiAuthenticationRequestDTO requestDTO) {
        log.info("PUT /api/register/authentication/{} - Updating API authentication", id);
        ApiAuthenticationResponseDTO response = apiAuthenticationService.updateApiAuthentication(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/authentication/{id}")
    public ResponseEntity<Void> deleteApiAuthentication(@PathVariable Long id) {
        log.info("DELETE /api/register/authentication/{} - Deleting API authentication", id);
        apiAuthenticationService.deleteApiAuthentication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/authentication/{id}/response-fields")
    public ResponseEntity<ApiAuthenticationResponseDTO> updateResponseFields(
            @PathVariable Long id,
            @Valid @RequestBody ResponseFieldsRequestDTO requestDTO) {
        log.info("PUT /api/register/authentication/{}/response-fields - Updating response fields", id);
        ApiAuthenticationResponseDTO response = apiAuthenticationService.updateResponseFields(id, requestDTO.getResponseFields());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authentication/test/{id}")
    public ResponseEntity<ApiAuthenticationTestResponseDTO> testApiAuthentication(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean registerTest) {
        log.info("POST /api/register/authentication/test/{}?registerTest={} - Testing API authentication", id, registerTest);
        ApiAuthenticationTestResponseDTO response = apiAuthenticationService.testApiAuthentication(id, registerTest);
        return ResponseEntity.ok(response);
    }
}
