package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerRequestDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;

    private String descricao;
}
