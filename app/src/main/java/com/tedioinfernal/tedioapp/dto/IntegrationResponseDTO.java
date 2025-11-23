package com.tedioinfernal.tedioapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationResponseDTO {

    private Long id;
    private String nome;
    private String description;
    private String baseUrl;
    private Long ownerId;
    private String ownerNome;
    private Long authenticationId;
    private String authenticationNome;
    private Map<String, String> headers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
