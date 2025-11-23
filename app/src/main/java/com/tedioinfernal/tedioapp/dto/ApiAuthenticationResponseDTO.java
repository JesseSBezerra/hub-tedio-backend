package com.tedioinfernal.tedioapp.dto;

import com.tedioinfernal.tedioapp.enums.AuthenticationType;
import com.tedioinfernal.tedioapp.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiAuthenticationResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Long ownerId;
    private String ownerNome;
    private String url;
    private AuthenticationType authenticationType;
    private ContentType contentType;
    private Map<String, Object> requestBody;
    private Map<String, String> headers;
    private Map<String, String> responseFields;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
