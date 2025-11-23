package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvolutionMessageRequestDTO {

    @NotBlank(message = "Número é obrigatório")
    private String number;

    @NotBlank(message = "Mensagem é obrigatória")
    private String message;

    @NotNull(message = "Evolution Instance ID é obrigatório")
    private Long evolutionInstanceId;
}
