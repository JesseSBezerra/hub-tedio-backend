package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationRequestDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    @Pattern(regexp = "^\\S+$", message = "O nome não pode conter espaços")
    private String nome;

    private String description;

    @NotBlank(message = "A base URL não pode estar vazia")
    private String baseUrl;

    @NotNull(message = "O ID do owner é obrigatório")
    private Long ownerId;

    private Long authenticationId;

    private Map<String, String> headers;
}
