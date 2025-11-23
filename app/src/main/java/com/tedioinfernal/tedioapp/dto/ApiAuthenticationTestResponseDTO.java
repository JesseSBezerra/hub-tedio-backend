package com.tedioinfernal.tedioapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiAuthenticationTestResponseDTO {

    private boolean success;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> responseHeaders;
    private String responseBody;
    private Map<String, String> extractedFields;
    private String errorMessage;
    private long responseTimeMs;
}
