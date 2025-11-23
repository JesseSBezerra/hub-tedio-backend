package com.tedioinfernal.tedioapp.controller;

import com.tedioinfernal.tedioapp.dto.RequestDTO;
import com.tedioinfernal.tedioapp.dto.RequestResponseDTO;
import com.tedioinfernal.tedioapp.dto.RequestTestResponseDTO;
import com.tedioinfernal.tedioapp.entity.User;
import com.tedioinfernal.tedioapp.security.UserContext;
import com.tedioinfernal.tedioapp.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/request")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestResponseDTO> createRequest(
            @Valid @RequestBody RequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/request - Creating request by user ID: {}", currentUser.getId());
        
        RequestResponseDTO response = requestService.createRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> getRequestById(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("GET /api/request/{} - Fetching request by user ID: {}", id, currentUser.getId());
        
        RequestResponseDTO response = requestService.getRequestById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> getAllRequests(
            @RequestParam(required = false) Long pathId) {
        
        User currentUser = UserContext.getCurrentUser();
        
        if (pathId != null) {
            log.info("GET /api/request?pathId={} - Fetching requests by path, user ID: {}", pathId, currentUser.getId());
            List<RequestResponseDTO> response = requestService.getRequestsByPathId(pathId);
            return ResponseEntity.ok(response);
        }
        
        log.info("GET /api/request - Fetching all requests by user ID: {}", currentUser.getId());
        List<RequestResponseDTO> response = requestService.getAllRequests();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestResponseDTO> updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody RequestDTO requestDTO) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("PUT /api/request/{} - Updating request by user ID: {}", id, currentUser.getId());
        
        RequestResponseDTO response = requestService.updateRequest(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("DELETE /api/request/{} - Deleting request by user ID: {}", id, currentUser.getId());
        
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test/{id}")
    public ResponseEntity<RequestTestResponseDTO> testRequest(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean registerTest) {
        
        User currentUser = UserContext.getCurrentUser();
        log.info("POST /api/request/test/{}?registerTest={} - Testing request by user ID: {}", id, registerTest, currentUser.getId());
        
        RequestTestResponseDTO response = requestService.testRequest(id, registerTest);
        return ResponseEntity.ok(response);
    }
}
