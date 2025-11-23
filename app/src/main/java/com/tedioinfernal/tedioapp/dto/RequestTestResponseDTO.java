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
public class RequestTestResponseDTO {

    private boolean success;
    private int statusCode;
    private String statusMessage;
    private String fullUrl;
    private String httpMethod;
    private Map<String, String> requestHeaders;
    private Map<String, String> requestParams;
    private Object requestBody;
    private Map<String, String> responseHeaders;
    private Object responseBody;
    private Map<String, String> extractedFields;
    private String errorMessage;
    private long responseTimeMs;
}
