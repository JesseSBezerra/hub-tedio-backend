package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotBlank(message = "URL é obrigatória")
    @Size(max = 1000, message = "URL deve ter no máximo 1000 caracteres")
    private String url;

    @NotBlank(message = "API Key é obrigatória")
    @Size(max = 500, message = "API Key deve ter no máximo 500 caracteres")
    private String apiKey;

    @NotNull(message = "Owner ID é obrigatório")
    private Long ownerId;
}
