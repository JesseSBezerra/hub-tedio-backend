package com.tedioinfernal.tedioapp.dto;

import com.tedioinfernal.tedioapp.enums.AuthenticationType;
import com.tedioinfernal.tedioapp.enums.ContentType;
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
public class ApiAuthenticationRequestDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    @Pattern(regexp = "^\\S+$", message = "O nome não pode conter espaços")
    private String nome;

    private String descricao;

    @NotNull(message = "O ID do owner é obrigatório")
    private Long ownerId;

    @NotBlank(message = "A URL não pode estar vazia")
    private String url;

    @NotNull(message = "O tipo de autenticação é obrigatório")
    private AuthenticationType authenticationType;

    @NotNull(message = "O content type é obrigatório")
    private ContentType contentType;

    private Map<String, Object> requestBody;

    private Map<String, String> headers;
}
