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
public class EvolutionAudioMessageRequestDTO {

    @NotBlank(message = "Número é obrigatório")
    private String number;

    @NotBlank(message = "Conteúdo do áudio (base64) é obrigatório")
    private String audio;

    @NotNull(message = "Evolution Instance ID é obrigatório")
    private Long evolutionInstanceId;
}
