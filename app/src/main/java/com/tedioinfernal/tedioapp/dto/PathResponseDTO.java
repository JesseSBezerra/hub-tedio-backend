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
public class PathResponseDTO {

    private Long id;
    private String nome;
    private String path;
    private Long integrationId;
    private String integrationNome;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
