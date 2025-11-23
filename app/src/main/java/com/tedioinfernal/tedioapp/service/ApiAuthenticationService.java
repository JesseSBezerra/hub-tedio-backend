package com.tedioinfernal.tedioapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedioinfernal.tedioapp.dto.ApiAuthenticationRequestDTO;
import com.tedioinfernal.tedioapp.dto.ApiAuthenticationResponseDTO;
import com.tedioinfernal.tedioapp.dto.ApiAuthenticationTestResponseDTO;
import com.tedioinfernal.tedioapp.entity.ApiAuthentication;
import com.tedioinfernal.tedioapp.entity.Owner;
import com.tedioinfernal.tedioapp.repository.ApiAuthenticationRepository;
import com.tedioinfernal.tedioapp.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiAuthenticationService {

    private final ApiAuthenticationRepository apiAuthenticationRepository;
    private final OwnerRepository ownerRepository;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    @Transactional
    public ApiAuthenticationResponseDTO createApiAuthentication(ApiAuthenticationRequestDTO requestDTO) {
        log.info("Creating API authentication with name: {}", requestDTO.getNome());

        Owner owner = ownerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner não encontrado"));

        ApiAuthentication apiAuth = ApiAuthentication.builder()
                .nome(requestDTO.getNome())
                .descricao(requestDTO.getDescricao())
                .owner(owner)
                .url(requestDTO.getUrl())
                .authenticationType(requestDTO.getAuthenticationType())
                .contentType(requestDTO.getContentType())
                .requestBody(requestDTO.getRequestBody())
                .headers(requestDTO.getHeaders())
                .build();

        ApiAuthentication savedApiAuth = apiAuthenticationRepository.save(apiAuth);
        log.info("API authentication created successfully with ID: {}", savedApiAuth.getId());

        return mapToResponseDTO(savedApiAuth);
    }

    @Transactional(readOnly = true)
    public ApiAuthenticationResponseDTO getApiAuthenticationById(Long id) {
        log.info("Fetching API authentication with ID: {}", id);
        ApiAuthentication apiAuth = apiAuthenticationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API authentication não encontrada"));
        return mapToResponseDTO(apiAuth);
    }

    @Transactional(readOnly = true)
    public ApiAuthenticationResponseDTO getApiAuthenticationByName(String nome) {
        log.info("Fetching API authentication with name: {}", nome);
        ApiAuthentication apiAuth = apiAuthenticationRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("API authentication não encontrada"));
        return mapToResponseDTO(apiAuth);
    }

    @Transactional(readOnly = true)
    public List<ApiAuthenticationResponseDTO> getAllApiAuthentications() {
        log.info("Fetching all API authentications");
        return apiAuthenticationRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ApiAuthenticationResponseDTO> getApiAuthenticationsByOwnerId(Long ownerId) {
        log.info("Fetching API authentications for owner ID: {}", ownerId);
        return apiAuthenticationRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApiAuthenticationResponseDTO updateApiAuthentication(Long id, ApiAuthenticationRequestDTO requestDTO) {
        log.info("Updating API authentication with ID: {}", id);

        ApiAuthentication apiAuth = apiAuthenticationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API authentication não encontrada"));

        Owner owner = ownerRepository.findById(requestDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner não encontrado"));

        apiAuth.setNome(requestDTO.getNome());
        apiAuth.setDescricao(requestDTO.getDescricao());
        apiAuth.setOwner(owner);
        apiAuth.setUrl(requestDTO.getUrl());
        apiAuth.setAuthenticationType(requestDTO.getAuthenticationType());
        apiAuth.setContentType(requestDTO.getContentType());
        apiAuth.setRequestBody(requestDTO.getRequestBody());
        apiAuth.setHeaders(requestDTO.getHeaders());

        ApiAuthentication updatedApiAuth = apiAuthenticationRepository.save(apiAuth);
        log.info("API authentication updated successfully with ID: {}", updatedApiAuth.getId());

        return mapToResponseDTO(updatedApiAuth);
    }

    @Transactional
    public void deleteApiAuthentication(Long id) {
        log.info("Deleting API authentication with ID: {}", id);

        if (!apiAuthenticationRepository.existsById(id)) {
            throw new RuntimeException("API authentication não encontrada");
        }

        apiAuthenticationRepository.deleteById(id);
        log.info("API authentication deleted successfully with ID: {}", id);
    }

    @Transactional
    public ApiAuthenticationResponseDTO updateResponseFields(Long id, Map<String, String> responseFields) {
        log.info("Updating response fields for API authentication ID: {}", id);

        ApiAuthentication apiAuth = apiAuthenticationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API authentication não encontrada"));

        apiAuth.setResponseFields(responseFields);
        ApiAuthentication updatedApiAuth = apiAuthenticationRepository.save(apiAuth);
        
        log.info("Response fields updated successfully for API authentication ID: {}", id);
        return mapToResponseDTO(updatedApiAuth);
    }

    public ApiAuthenticationTestResponseDTO testApiAuthentication(Long id, boolean registerTest) {
        log.info("Testing API authentication with ID: {}", id);

        ApiAuthentication apiAuth = apiAuthenticationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API authentication não encontrada"));

        long startTime = System.currentTimeMillis();

        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(apiAuth.getUrl()))
                    .timeout(Duration.ofSeconds(30));

            // Adiciona headers
            if (apiAuth.getHeaders() != null) {
                apiAuth.getHeaders().forEach(requestBuilder::header);
            }

            // Adiciona Content-Type
            requestBuilder.header("Content-Type", apiAuth.getContentType().getValue());

            // Adiciona body se existir
            if (apiAuth.getRequestBody() != null && !apiAuth.getRequestBody().isEmpty()) {
                String body;
                
                // Verifica o tipo de conteúdo para formatar o body corretamente
                if (apiAuth.getContentType().getValue().contains("application/x-www-form-urlencoded")) {
                    // Formato: key1=value1&key2=value2
                    body = apiAuth.getRequestBody().entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.joining("&"));
                } else {
                    // Formato JSON para outros content types
                    body = objectMapper.writeValueAsString(apiAuth.getRequestBody());
                }
                
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));
            } else {
                requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            long endTime = System.currentTimeMillis();

            Map<String, String> responseHeaders = new HashMap<>();
            response.headers().map().forEach((key, values) -> 
                responseHeaders.put(key, String.join(", ", values))
            );

            // Extrai campos do response JSON
            Map<String, String> extractedFields = extractFieldsFromJson(response.body());

            // Se registerTest=true, salva os campos extraídos na autenticação
            if (registerTest && !extractedFields.isEmpty()) {
                apiAuth.setResponseFields(extractedFields);
                apiAuthenticationRepository.save(apiAuth);
                log.info("Response fields registered for API authentication ID: {}", id);
            }

            return ApiAuthenticationTestResponseDTO.builder()
                    .success(response.statusCode() >= 200 && response.statusCode() < 300)
                    .statusCode(response.statusCode())
                    .statusMessage(getStatusMessage(response.statusCode()))
                    .responseHeaders(responseHeaders)
                    .responseBody(response.body())
                    .extractedFields(extractedFields)
                    .responseTimeMs(endTime - startTime)
                    .build();

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("Error testing API authentication: {}", e.getMessage());

            return ApiAuthenticationTestResponseDTO.builder()
                    .success(false)
                    .statusCode(0)
                    .errorMessage(e.getMessage())
                    .responseTimeMs(endTime - startTime)
                    .build();
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

    private Map<String, String> extractFieldsFromJson(String jsonBody) {
        Map<String, String> fields = new HashMap<>();
        
        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            return fields;
        }

        try {
            Object parsed = objectMapper.readValue(jsonBody, Object.class);
            extractFieldsRecursive(parsed, "", fields);
        } catch (Exception e) {
            log.warn("Failed to parse JSON response: {}", e.getMessage());
        }

        return fields;
    }

    @SuppressWarnings("unchecked")
    private void extractFieldsRecursive(Object obj, String prefix, Map<String, String> fields) {
        if (obj == null) {
            fields.put(prefix.isEmpty() ? "null" : prefix, "null");
            return;
        }

        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;
                extractFieldsRecursive(entry.getValue(), newPrefix, fields);
            }
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            if (!list.isEmpty()) {
                // Para arrays, usa o formato: campo[]
                String arrayPrefix = prefix + "[]";
                extractFieldsRecursive(list.get(0), arrayPrefix, fields);
            } else {
                fields.put(prefix + "[]", "array");
            }
        } else {
            // Determina o tipo do valor
            String type = determineType(obj);
            fields.put(prefix, type);
        }
    }

    private String determineType(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "string";
        } else if (value instanceof Integer || value instanceof Long) {
            return "integer";
        } else if (value instanceof Double || value instanceof Float) {
            return "number";
        } else if (value instanceof Boolean) {
            return "boolean";
        } else {
            return "object";
        }
    }

    private ApiAuthenticationResponseDTO mapToResponseDTO(ApiAuthentication apiAuth) {
        return ApiAuthenticationResponseDTO.builder()
                .id(apiAuth.getId())
                .nome(apiAuth.getNome())
                .descricao(apiAuth.getDescricao())
                .ownerId(apiAuth.getOwner().getId())
                .ownerNome(apiAuth.getOwner().getNome())
                .url(apiAuth.getUrl())
                .authenticationType(apiAuth.getAuthenticationType())
                .contentType(apiAuth.getContentType())
                .requestBody(apiAuth.getRequestBody())
                .headers(apiAuth.getHeaders())
                .responseFields(apiAuth.getResponseFields())
                .createdAt(apiAuth.getCreatedAt())
                .updatedAt(apiAuth.getUpdatedAt())
                .build();
    }
}
