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
public class EvolutionInstanceRequestDTO {

    @NotBlank(message = "Nome da instância é obrigatório")
    @Size(min = 3, max = 255, message = "Nome da instância deve ter entre 3 e 255 caracteres")
    private String instanceName;

    @NotNull(message = "QRCode é obrigatório")
    private Boolean qrcode;

    @NotNull(message = "Evolution ID é obrigatório")
    private Long evolutionId;

    @NotBlank(message = "Tipo de integração é obrigatório")
    @Size(max = 100, message = "Tipo de integração deve ter no máximo 100 caracteres")
    private String integration;
}
