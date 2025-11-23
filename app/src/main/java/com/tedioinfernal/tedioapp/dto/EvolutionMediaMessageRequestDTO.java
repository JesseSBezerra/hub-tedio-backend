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
public class EvolutionMediaMessageRequestDTO {

    @NotBlank(message = "Número é obrigatório")
    private String number;

    @NotBlank(message = "Tipo de mídia é obrigatório")
    private String mediatype;

    @NotBlank(message = "MIME type é obrigatório")
    private String mimetype;

    @NotBlank(message = "Conteúdo da mídia (base64) é obrigatório")
    private String media;

    @NotBlank(message = "Nome do arquivo é obrigatório")
    private String fileName;

    @NotNull(message = "Evolution Instance ID é obrigatório")
    private Long evolutionInstanceId;
}
