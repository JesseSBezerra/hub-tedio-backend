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
public class PathRequestDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;

    @NotBlank(message = "O path não pode estar vazio")
    private String path;

    @NotNull(message = "O ID da integração é obrigatório")
    private Long integrationId;
}
