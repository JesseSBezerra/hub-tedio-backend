package com.tedioinfernal.tedioapp.dto;

import com.tedioinfernal.tedioapp.enums.ContentType;
import com.tedioinfernal.tedioapp.enums.HttpMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;

    @NotNull(message = "O método HTTP é obrigatório")
    private HttpMethod httpMethod;

    @NotNull(message = "O ID do path é obrigatório")
    private Long pathId;

    private ContentType contentType;

    private Map<String, String> bodyFields;

    private Map<String, String> headerFields;

    private Map<String, String> paramFields;

    private Map<String, Object> requestExample;
}
