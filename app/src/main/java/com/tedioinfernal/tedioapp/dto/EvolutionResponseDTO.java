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
public class EvolutionResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private String url;
    private String apiKey;
    private Long ownerId;
    private String ownerNome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
