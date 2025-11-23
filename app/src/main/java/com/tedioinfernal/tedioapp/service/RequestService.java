package com.tedioinfernal.tedioapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedioinfernal.tedioapp.dto.ApiAuthenticationTestResponseDTO;
import com.tedioinfernal.tedioapp.dto.RequestDTO;
import com.tedioinfernal.tedioapp.dto.RequestResponseDTO;
import com.tedioinfernal.tedioapp.dto.RequestTestResponseDTO;
import com.tedioinfernal.tedioapp.entity.ApiAuthentication;
import com.tedioinfernal.tedioapp.entity.Integration;
import com.tedioinfernal.tedioapp.entity.Path;
import com.tedioinfernal.tedioapp.entity.Request;
import com.tedioinfernal.tedioapp.repository.PathRepository;
import com.tedioinfernal.tedioapp.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final PathRepository pathRepository;
    private final ObjectMapper objectMapper;
    private final ApiAuthenticationService apiAuthenticationService;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    @Transactional
    public RequestResponseDTO createRequest(RequestDTO requestDTO) {
        log.info("Creating request with name: {}", requestDTO.getNome());

        Path path = pathRepository.findById(requestDTO.getPathId())
                .orElseThrow(() -> new RuntimeException("Path não encontrado"));

        Request request = Request.builder()
                .nome(requestDTO.getNome())
                .httpMethod(requestDTO.getHttpMethod())
                .path(path)
                .contentType(requestDTO.getContentType())
                .bodyFields(requestDTO.getBodyFields())
                .headerFields(requestDTO.getHeaderFields())
                .paramFields(requestDTO.getParamFields())
                .requestExample(requestDTO.getRequestExample())
                .build();

        Request savedRequest = requestRepository.save(request);
        log.info("Request created successfully with ID: {}", savedRequest.getId());

        return mapToResponseDTO(savedRequest);
    }

    @Transactional(readOnly = true)
    public RequestResponseDTO getRequestById(Long id) {
        log.info("Fetching request with ID: {}", id);
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request não encontrada"));
        return mapToResponseDTO(request);
    }

    @Transactional(readOnly = true)
    public List<RequestResponseDTO> getAllRequests() {
        log.info("Fetching all requests");
        return requestRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequestResponseDTO> getRequestsByPathId(Long pathId) {
        log.info("Fetching requests for path ID: {}", pathId);
        return requestRepository.findByPathId(pathId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequestResponseDTO updateRequest(Long id, RequestDTO requestDTO) {
        log.info("Updating request with ID: {}", id);

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request não encontrada"));

        Path path = pathRepository.findById(requestDTO.getPathId())
                .orElseThrow(() -> new RuntimeException("Path não encontrado"));

        request.setNome(requestDTO.getNome());
        request.setHttpMethod(requestDTO.getHttpMethod());
        request.setPath(path);
        request.setContentType(requestDTO.getContentType());
        request.setBodyFields(requestDTO.getBodyFields());
        request.setHeaderFields(requestDTO.getHeaderFields());
        request.setParamFields(requestDTO.getParamFields());
        request.setRequestExample(requestDTO.getRequestExample());

        Request updatedRequest = requestRepository.save(request);
        log.info("Request updated successfully with ID: {}", updatedRequest.getId());

        return mapToResponseDTO(updatedRequest);
    }

    @Transactional
    public void deleteRequest(Long id) {
        log.info("Deleting request with ID: {}", id);

        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request não encontrada");
        }

        requestRepository.deleteById(id);
        log.info("Request deleted successfully with ID: {}", id);
    }

    public RequestTestResponseDTO testRequest(Long id, boolean registerTest) {
        log.info("Testing request with ID: {}, registerTest: {}", id, registerTest);

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request não encontrada"));

        Path path = request.getPath();
        Integration integration = path.getIntegration();
        ApiAuthentication authentication = integration.getAuthentication();

        // Monta URL completa: baseUrl + path
        String fullUrl = integration.getBaseUrl() + path.getPath();

        long startTime = System.currentTimeMillis();

        try {
            // Executa o teste de autenticação primeiro, se houver
            final Map<String, Object> authResponseData = new HashMap<>();
            if (authentication != null) {
                log.info("Executing authentication test for authentication ID: {}", authentication.getId());
                ApiAuthenticationTestResponseDTO authTestResponse = apiAuthenticationService.testApiAuthentication(authentication.getId(), false);
                
                if (authTestResponse.isSuccess()) {
                    // Parse do response body para extrair os campos
                    Map<String, Object> parsedData = parseAuthenticationResponse(authTestResponse.getResponseBody());
                    authResponseData.putAll(parsedData);
                    log.info("Authentication test successful. Extracted {} fields", authResponseData.size());
                } else {
                    log.warn("Authentication test failed with status: {}", authTestResponse.getStatusCode());
                }
            }

            // Monta a URL com parâmetros de query se houver
            String urlWithParams = buildUrlWithParams(fullUrl, request.getRequestExample());

            java.net.http.HttpRequest.Builder requestBuilder = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(urlWithParams))
                    .timeout(Duration.ofSeconds(30));

            // Adiciona headers da integração (com substituição de variáveis)
            if (integration.getHeaders() != null) {
                for (Map.Entry<String, String> header : integration.getHeaders().entrySet()) {
                    String headerValue = replaceVariables(header.getValue(), authResponseData);
                    requestBuilder.header(header.getKey(), headerValue);
                }
            }

            // Adiciona headers da request (com substituição de variáveis)
            if (request.getHeaderFields() != null) {
                request.getHeaderFields().forEach((key, value) -> {
                    if (request.getRequestExample() != null && request.getRequestExample().containsKey(key)) {
                        String headerValue = replaceVariables(String.valueOf(request.getRequestExample().get(key)), authResponseData);
                        requestBuilder.header(key, headerValue);
                    }
                });
            }

            // Configura método HTTP e body
            String body = null;
            if (request.getHttpMethod().toString().equals("GET") || request.getHttpMethod().toString().equals("DELETE")) {
                requestBuilder.method(request.getHttpMethod().toString(), BodyPublishers.noBody());
            } else {
                // Monta o body baseado no requestExample
                if (request.getContentType() != null && request.getRequestExample() != null) {
                    if (request.getContentType().getValue().contains("application/x-www-form-urlencoded")) {
                        body = buildFormUrlencodedBody(request.getRequestExample());
                        requestBuilder.header("Content-Type", request.getContentType().getValue());
                    } else {
                        body = objectMapper.writeValueAsString(request.getRequestExample());
                        requestBuilder.header("Content-Type", request.getContentType().getValue());
                    }
                    requestBuilder.method(request.getHttpMethod().toString(), BodyPublishers.ofString(body));
                } else {
                    requestBuilder.method(request.getHttpMethod().toString(), BodyPublishers.noBody());
                }
            }

            java.net.http.HttpRequest httpRequest = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            long endTime = System.currentTimeMillis();

            Map<String, String> responseHeaders = new HashMap<>();
            response.headers().map().forEach((key, values) ->
                    responseHeaders.put(key, String.join(", ", values))
            );

            // Extrai campos do response JSON
            Map<String, String> extractedFields = extractFieldsWithTypes(response.body());

            // Se registerTest=true, salva os campos extraídos e o exemplo
            if (registerTest) {
                boolean updated = false;
                
                // Atualiza responseFields se houver
                if (!extractedFields.isEmpty()) {
                    request.setResponseFields(extractedFields);
                    
                    // Salva o response completo como exemplo
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> responseExample = objectMapper.readValue(response.body(), Map.class);
                        request.setResponseExample(responseExample);
                    } catch (Exception e) {
                        log.warn("Failed to parse response as JSON for example: {}", e.getMessage());
                    }
                    updated = true;
                }
                
                // Atualiza bodyFields se for POST/PUT/PATCH e tiver requestExample
                if (request.getRequestExample() != null && 
                    (request.getHttpMethod().toString().equals("POST") || 
                     request.getHttpMethod().toString().equals("PUT") || 
                     request.getHttpMethod().toString().equals("PATCH"))) {
                    
                    try {
                        // Serializa o requestExample para JSON e extrai os campos
                        String requestBodyJson = objectMapper.writeValueAsString(request.getRequestExample());
                        Map<String, String> bodyFieldsExtracted = extractFieldsWithTypes(requestBodyJson);
                        
                        if (!bodyFieldsExtracted.isEmpty()) {
                            request.setBodyFields(bodyFieldsExtracted);
                            log.info("Body fields extracted for request ID: {}", id);
                            updated = true;
                        }
                    } catch (Exception e) {
                        log.warn("Failed to extract body fields from requestExample: {}", e.getMessage());
                    }
                }
                
                if (updated) {
                    requestRepository.save(request);
                    log.info("Fields registered for request ID: {}", id);
                }
            }

            return RequestTestResponseDTO.builder()
                    .success(response.statusCode() >= 200 && response.statusCode() < 300)
                    .statusCode(response.statusCode())
                    .statusMessage(getStatusMessage(response.statusCode()))
                    .fullUrl(urlWithParams)
                    .httpMethod(request.getHttpMethod().toString())
                    .requestHeaders(extractRequestHeaders(httpRequest))
                    .requestParams(extractParamsFromExample(request.getRequestExample()))
                    .requestBody(parseJsonToObject(body))
                    .responseHeaders(responseHeaders)
                    .responseBody(parseJsonToObject(response.body()))
                    .extractedFields(extractedFields)
                    .responseTimeMs(endTime - startTime)
                    .build();

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("Error testing request: {}", e.getMessage());

            return RequestTestResponseDTO.builder()
                    .success(false)
                    .statusCode(0)
                    .statusMessage("Error")
                    .fullUrl(fullUrl)
                    .httpMethod(request.getHttpMethod().toString())
                    .errorMessage(e.getMessage())
                    .responseTimeMs(endTime - startTime)
                    .build();
        }
    }

    private String buildUrlWithParams(String baseUrl, Map<String, Object> requestExample) {
        if (requestExample == null || !requestExample.containsKey("params")) {
            return baseUrl;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) requestExample.get("params");
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }

        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        params.forEach((key, value) -> url.append(key).append("=").append(value).append("&"));
        return url.substring(0, url.length() - 1);
    }

    private String buildFormUrlencodedBody(Map<String, Object> requestExample) {
        if (requestExample == null) {
            return "";
        }
        return requestExample.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("params"))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private Map<String, Object> parseAuthenticationResponse(String responseBody) {
        Map<String, Object> result = new HashMap<>();
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return result;
        }

        try {
            // Parse JSON response
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonMap = objectMapper.readValue(responseBody, Map.class);
            flattenMap("", jsonMap, result);
        } catch (Exception e) {
            log.warn("Failed to parse authentication response: {}", e.getMessage());
        }

        return result;
    }

    private void flattenMap(String prefix, Map<String, Object> map, Map<String, Object> result) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nestedMap = (Map<String, Object>) value;
                flattenMap(key, nestedMap, result);
            } else if (value instanceof List) {
                result.put(key, value);
                // Também adiciona elementos individuais se for lista de objetos simples
                List<?> list = (List<?>) value;
                for (int i = 0; i < list.size(); i++) {
                    result.put(key + "[" + i + "]", list.get(i));
                }
            } else {
                result.put(key, value);
            }
        }
    }

    private String replaceVariables(String text, Map<String, Object> variables) {
        if (text == null || !text.contains("${")) {
            return text;
        }

        String result = text;
        // Regex para encontrar ${variavel}
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\$\\{([^}]+)\\}");
        java.util.regex.Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);
            
            if (value != null) {
                result = result.replace("${" + variableName + "}", String.valueOf(value));
                log.debug("Replaced variable ${} with value: {}", variableName, value);
            } else {
                log.warn("Variable ${} not found in authentication response", variableName);
            }
        }

        return result;
    }

    private Map<String, String> extractRequestHeaders(java.net.http.HttpRequest httpRequest) {
        Map<String, String> headers = new HashMap<>();
        httpRequest.headers().map().forEach((key, values) ->
                headers.put(key, String.join(", ", values))
        );
        return headers;
    }

    private Map<String, String> extractParamsFromExample(Map<String, Object> requestExample) {
        if (requestExample == null || !requestExample.containsKey("params")) {
            return new HashMap<>();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) requestExample.get("params");
        Map<String, String> result = new HashMap<>();
        if (params != null) {
            params.forEach((key, value) -> result.put(key, String.valueOf(value)));
        }
        return result;
    }

    private Map<String, String> extractFieldsWithTypes(String responseBody) {
        Map<String, String> fields = new HashMap<>();
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return fields;
        }

        try {
            Object parsed = objectMapper.readValue(responseBody, Object.class);
            extractFieldsRecursively("", parsed, fields);
        } catch (Exception e) {
            log.warn("Failed to extract fields from response: {}", e.getMessage());
        }

        return fields;
    }

    private void extractFieldsRecursively(String prefix, Object value, Map<String, String> fields) {
        if (value == null) {
            fields.put(prefix.isEmpty() ? "null" : prefix, "null");
            return;
        }

        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            
            if (prefix.isEmpty()) {
                // Raiz é um objeto
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    extractFieldsRecursively(entry.getKey(), entry.getValue(), fields);
                }
            } else {
                // Objeto aninhado
                fields.put(prefix, "object");
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String newPrefix = prefix + "." + entry.getKey();
                    extractFieldsRecursively(newPrefix, entry.getValue(), fields);
                }
            }
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            fields.put(prefix, "array");
            
            if (!list.isEmpty()) {
                Object firstElement = list.get(0);
                if (firstElement instanceof Map || firstElement instanceof List) {
                    // Array de objetos ou arrays
                    extractFieldsRecursively(prefix + "[]", firstElement, fields);
                } else {
                    // Array de primitivos
                    fields.put(prefix + "[]", getSimpleType(firstElement));
                }
            }
        } else {
            // Tipo primitivo
            fields.put(prefix, getSimpleType(value));
        }
    }

    private String getSimpleType(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return "string";
        if (value instanceof Integer || value instanceof Long) return "integer";
        if (value instanceof Double || value instanceof Float) return "number";
        if (value instanceof Boolean) return "boolean";
        return "unknown";
    }

    private Object parseJsonToObject(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            // Tenta parsear como JSON e retornar como objeto
            return objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            // Se não for JSON válido, retorna a string original
            return json;
        }
    }

    private String getStatusMessage(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Status " + statusCode;
        };
    }

    private RequestResponseDTO mapToResponseDTO(Request request) {
        return RequestResponseDTO.builder()
                .id(request.getId())
                .nome(request.getNome())
                .httpMethod(request.getHttpMethod())
                .pathId(request.getPath().getId())
                .pathNome(request.getPath().getNome())
                .pathValue(request.getPath().getPath())
                .contentType(request.getContentType())
                .bodyFields(request.getBodyFields())
                .headerFields(request.getHeaderFields())
                .paramFields(request.getParamFields())
                .requestExample(request.getRequestExample())
                .responseFields(request.getResponseFields())
                .responseExample(request.getResponseExample())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}
