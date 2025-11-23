package com.tedioinfernal.tedioapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionInstanceResponseDTO {

    private Long id;
    private String instanceName;
    private String instanceId;
    private Boolean qrcode;
    private String qrcodeBase64;
    private String integration;
    private String status;
    private String hash;
    private String webhookUrl;
    private Long evolutionId;
    private String evolutionNome;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
