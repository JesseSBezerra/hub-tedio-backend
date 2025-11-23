package com.tedioinfernal.tedioapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionRequestDTO {

    @NotNull(message = "O array de permissões não pode ser nulo")
    private List<String> permissions;
}
