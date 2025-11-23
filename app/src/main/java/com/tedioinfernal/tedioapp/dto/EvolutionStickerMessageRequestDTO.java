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
public class EvolutionStickerMessageRequestDTO {

    @NotBlank(message = "Número é obrigatório")
    private String number;

    @NotBlank(message = "Conteúdo do sticker (base64) é obrigatório")
    private String sticker;

    @NotNull(message = "Evolution Instance ID é obrigatório")
    private Long evolutionInstanceId;
}
