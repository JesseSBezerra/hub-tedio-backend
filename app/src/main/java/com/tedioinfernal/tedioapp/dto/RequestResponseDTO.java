package com.tedioinfernal.tedioapp.dto;

import com.tedioinfernal.tedioapp.enums.ContentType;
import com.tedioinfernal.tedioapp.enums.HttpMethod;
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
public class RequestResponseDTO {

    private Long id;
    private String nome;
    private HttpMethod httpMethod;
    private Long pathId;
    private String pathNome;
    private String pathValue;
    private ContentType contentType;
    private Map<String, String> bodyFields;
    private Map<String, String> headerFields;
    private Map<String, String> paramFields;
    private Map<String, Object> requestExample;
    private Map<String, String> responseFields;
    private Map<String, Object> responseExample;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
